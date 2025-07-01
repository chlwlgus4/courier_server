package com.courier.orders.repository;

import com.courier.orders.domain.Orders;
import com.courier.orders.enums.OrderStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findByUserId(Long userId, Pageable pageable);

    Optional<Orders> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT o FROM Orders o WHERE o.userId = :userId " +
            "AND (:status IS NULL OR :status = '' OR o.status = :status) " +
            "AND (:search IS NULL OR :search = '' OR " +
            "     LOWER(o.originAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "     LOWER(o.destinationAddress) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "     LOWER(o.notes) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:startDate IS NULL OR DATE(o.createdDate) >= :startDate) " +
            "AND (:endDate IS NULL OR DATE(o.createdDate) <= :endDate)")
    Page<Orders> findOrdersWithFilters(@Param("userId") Long userId,
                                       @Param("status") OrderStatus status,
                                       @Param("search") String search,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate,
                                       Pageable pageable);


}
