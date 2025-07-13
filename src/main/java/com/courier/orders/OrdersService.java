package com.courier.orders;

import com.courier.handler.exception.BadRequestException;
import com.courier.handler.exception.ResourceNotFoundException;
import com.courier.handler.exception.UnauthorizedException;
import com.courier.orders.domain.OrderImage;
import com.courier.orders.domain.Orders;
import com.courier.orders.dto.OrderGetResponse;
import com.courier.orders.dto.OrderImageResponse;
import com.courier.orders.dto.OrderListResponse;
import com.courier.orders.dto.OrderSaveRequest;
import com.courier.orders.enums.OrderStatus;
import com.courier.orders.repository.OrderImageRepository;
import com.courier.orders.repository.OrdersRepository;
import com.courier.shipping.domain.ShippingType;
import com.courier.shipping.repository.ShippingRepository;
import com.courier.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderImageRepository orderImageRepository;
    private final ShippingRepository shippingRepository;
    private final ModelMapper modelMapper;

    @Value("${file.upload.path:/tmp/uploads}")
    private String uploadPath;

    public OrderListResponse getOrders(Pageable pageable, OrderStatus status, String search, LocalDate startDate, LocalDate endDate) {
        Long userId = AuthUtil.getCurrentUserId();
        if (userId == null) throw new UnauthorizedException("로그인이 필요합니다.");

        Page<Orders> ordersPage;

        if (hasFilters(status, search, startDate, endDate)) {
            ordersPage = ordersRepository.findOrdersWithFilters(userId, status, search, startDate, endDate, pageable);
        } else {
            ordersPage = ordersRepository.findByUserId(userId, pageable);
        }

        List<OrderGetResponse> orderResponses = ordersPage.getContent().stream()
                .map(this::convertToOrderGetResponse)
                .toList();

        return new OrderListResponse(
                orderResponses,
                ordersPage.hasNext(),
                (int) ordersPage.getTotalElements(),
                ordersPage.getNumber(),
                ordersPage.getSize()
        );
    }

    private boolean hasFilters(OrderStatus status, String search, LocalDate startDate, LocalDate endDate) {
        return status != null ||
                (search != null && !search.trim().isEmpty()) ||
                startDate != null ||
                endDate != null;
    }

    public OrderGetResponse getOrder(Long orderId) {
        Long userId = AuthUtil.getCurrentUserId();
        if (userId == null) throw new UnauthorizedException("로그인이 필요합니다.");

        Orders order = ordersRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("주문 정보가 없습니다."));

        return convertToOrderGetResponse(order);
    }

    private OrderGetResponse convertToOrderGetResponse(Orders order) {

        log.info("order {}", order);
        List<OrderImageResponse> orderImages = getOrderImagesWithData(order.getId());
        ShippingType shippingType = shippingRepository.findByCode(order.getShippingTypeCode());

        return OrderGetResponse.builder()
                .shippingType(shippingType.getName())
                .weight(order.getWeight())
                .insuranceValue(order.getInsuranceValue())
                .status(order.getStatus())
                .originCountry(order.getOriginCountry())
                .destinationCountry(order.getDestinationCountry())
                .originPostalCode(order.getOriginPostalCode())
                .originAddress(order.getOriginAddress())
                .originAddressDetail(order.getOriginAddressDetail())
                .destinationPostalCode(order.getDestinationPostalCode())
                .destinationAddress(order.getDestinationAddress())
                .destinationAddressDetail(order.getDestinationAddressDetail())
                .notes(order.getNotes())
                .images(orderImages)
                .build();
    }

    private List<OrderImageResponse> getOrderImagesWithData(Long orderId) {
        List<OrderImage> orderImages = orderImageRepository.findByOrderId(orderId.intValue());

        return orderImages.stream()
                .map(this::convertToImageResponse)
                .toList();
    }

    private OrderImageResponse convertToImageResponse(OrderImage orderImage) {
        try {
            // 파일 데이터를 Base64로 인코딩
            String base64Data = encodeImageToBase64(orderImage.getImagePath());

            return OrderImageResponse.builder()
                    .id(orderImage.getId())
                    .imagePath(orderImage.getImagePath())
                    .originalFilename(orderImage.getOriginalFilename())
                    .fileSize(orderImage.getFileSize())
                    .contentType(orderImage.getContentType())
                    .imageOrder(orderImage.getImageOrder())
                    .base64Data(base64Data)
                    .createdDate(orderImage.getCreatedDate())
                    .build();
        } catch (Exception e) {
            log.error("이미지 데이터 읽기 실패: {}", orderImage.getImagePath(), e);
            // 파일 읽기 실패시 base64Data 없이 반환
            return OrderImageResponse.builder()
                    .id(orderImage.getId())
                    .imagePath(orderImage.getImagePath())
                    .originalFilename(orderImage.getOriginalFilename())
                    .fileSize(orderImage.getFileSize())
                    .contentType(orderImage.getContentType())
                    .imageOrder(orderImage.getImageOrder())
                    .base64Data(null)
                    .createdDate(orderImage.getCreatedDate())
                    .build();
        }
    }

    private String encodeImageToBase64(String relativePath) throws IOException {
        String fullPath = uploadPath + File.separator + relativePath;
        Path imagePath = Paths.get(fullPath);

        if (!Files.exists(imagePath)) {
            throw new IOException("이미지 파일이 존재하지 않습니다: " + fullPath);
        }

        byte[] imageBytes = Files.readAllBytes(imagePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Transactional(rollbackFor = Exception.class)
    public Long save(OrderSaveRequest dto) {
        Long userId = AuthUtil.getCurrentUserId();

        if (userId == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        List<String> savedImagePaths = new ArrayList<>(); // 저장된 파일 경로 추적

        dto.setShippingTypeCode(dto.getShippingTypeCode().toUpperCase());

        try {

            String trackingNumber = generateTrackingNumber();

            // 주문 저장
            Orders order = modelMapper.map(dto, Orders.class);
            order.setTrackingNumber(trackingNumber);
            order.setOriginCountry(dto.getOriginCountryCode());
            order.setDestinationCountry(dto.getDestinationCountryCode());
            order.setUserId(userId);
            Orders savedOrder = ordersRepository.save(order);

            // 이미지 저장
            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                savedImagePaths.addAll(saveOrderImages(savedOrder.getId(), dto.getImages()));
            }

            log.info("주문 저장 완료 - Order ID: {}, Images: {}", savedOrder.getId(), savedImagePaths.size());
            return order.getId();
        } catch (Exception e) {
            // 예외 발생시 저장된 파일들 삭제
            if (!savedImagePaths.isEmpty()) {
                deleteFiles(savedImagePaths);
                log.info("파일 롤백 완료: {} 개 파일 삭제", savedImagePaths.size());
            }
            log.error("주문 저장 실패: {}", e.getMessage());
            throw e; // 예외 재발생으로 DB도 롤백
        }
    }

    private String generateTrackingNumber() {
        String trackingNumber;
        do {
            trackingNumber = "ORD-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                    + "-" + UUID.randomUUID().toString().replace("-", "")
                    .substring(0, 6).toUpperCase();
        } while (ordersRepository.existsByTrackingNumberEquals(trackingNumber));
        return trackingNumber;
    }

    private List<String> saveOrderImages(Long orderId, List<MultipartFile> images) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderImage> orderImages = new ArrayList<>();
        List<String> savedPaths = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            if (!image.isEmpty()) {
                try {
                    String savedPath = saveImageFile(image);
                    savedPaths.add(savedPath); // 저장된 경로 추가

                    OrderImage orderImage = OrderImage.builder()
                            .order(order)
                            .imagePath(savedPath)
                            .originalFilename(image.getOriginalFilename())
                            .fileSize(image.getSize())
                            .contentType(image.getContentType())
                            .imageOrder(i + 1)
                            .build();

                    orderImages.add(orderImage);
                } catch (IOException e) {
                    // 이미 저장된 파일들 삭제
                    deleteFiles(savedPaths);
                    log.error(e.getMessage(), e);
                    throw new BadRequestException("이미지 저장에 실패했습니다.");
                }
            }
        }

        if (!orderImages.isEmpty()) {
            orderImageRepository.saveAll(orderImages);
        }

        return savedPaths; // 저장된 경로 리스트 반환
    }

    private String saveImageFile(MultipartFile image) throws IOException {
        // 업로드 디렉토리 생성
        String dateFolder = LocalDate.now().toString(); // yyyy-MM-dd 형식
        String uploadDir = uploadPath + File.separator + "orders" + File.separator + dateFolder;

        Path uploadDirPath = Paths.get(uploadDir);
        if (!Files.exists(uploadDirPath)) {
            Files.createDirectories(uploadDirPath);
        }

        // 파일명 생성 (UUID + 원본 확장자)
        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + extension;
        String filePath = uploadDir + File.separator + filename;

        // 파일 저장
        Path destinationPath = Paths.get(filePath);
        Files.copy(image.getInputStream(), destinationPath);

        // 상대 경로 반환 (DB에 저장될 경로)
        return "orders" + File.separator + dateFolder + File.separator + filename;
    }

    // 파일 삭제 메서드 추가
    private void deleteFiles(List<String> savedPaths) {
        for (String relativePath : savedPaths) {
            try {
                String fullPath = uploadPath + File.separator + relativePath;
                Path filePath = Paths.get(fullPath);
                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                }
            } catch (IOException e) {
                log.error("파일 삭제 실패: {}", relativePath);
            }
        }
    }
}
