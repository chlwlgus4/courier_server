package com.courier.hstariff.repository;

import com.courier.hstariff.domain.HsTariff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HsTariffRepository extends JpaRepository<HsTariff, Long> {
    Optional<HsTariff> findByHsCodeAndStartDate(String hsCode, LocalDate startDate);
}
