package com.courier.shipping.repository;

import com.courier.shipping.domain.ShippingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<ShippingType, Long> {
    ShippingType findByCode(String shippingTypeCode);
}
