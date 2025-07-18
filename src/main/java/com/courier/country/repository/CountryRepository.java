package com.courier.country.repository;

import com.courier.country.domain.Country;
import com.courier.country.enums.UseYn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {
    List<Country> findByUseYn(UseYn useYn);
}
