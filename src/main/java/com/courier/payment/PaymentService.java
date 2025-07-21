package com.courier.payment;

import com.courier.handler.exception.PaymentException;
import com.courier.payment.dto.PaymentConfirmRequest;
import com.courier.payment.dto.PaymentConfirmResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${payment.secret}")
    private String secret;
    @Value("${payment.url}")
    private String paymentUrl;

    private final RestTemplate restTemplate;

    public PaymentConfirmResponse confirm(PaymentConfirmRequest request) {
        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        String auth = Base64.getEncoder()
                .encodeToString((secret + ":").getBytes(StandardCharsets.UTF_8));

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        // 요청 본문과 헤더를 포함한 엔티티 생성
        HttpEntity<PaymentConfirmRequest> entity = new HttpEntity<>(request, headers);

        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        try {
            ResponseEntity<PaymentConfirmResponse> response = restTemplate.exchange(
                    paymentUrl,
                    HttpMethod.POST,
                    entity,
                    PaymentConfirmResponse.class
            );

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("토스페이먼츠 API 에러: {}, 응답: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new PaymentException("결제 승인에 실패했습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("결제 승인 중 예외 발생", e);
            throw new PaymentException("결제 승인 중 오류가 발생했습니다");
        }

    }
}
