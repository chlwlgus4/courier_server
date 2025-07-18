package com.courier.country;

import com.courier.country.domain.Country;
import com.courier.country.dto.CountryAllResponse;
import com.courier.country.enums.UseYn;
import com.courier.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public List<CountryAllResponse> getCountryAll() {
        return countryRepository.findByUseYn(UseYn.Y).stream()
                .map(this::convertToCountryAllResponse)
                .toList();
    }

    private CountryAllResponse convertToCountryAllResponse(Country country) {
        return CountryAllResponse.builder().id(country.getId())
                .nameKo(country.getNameKo())
                .nameEn(country.getNameEn())
                .build();
    }
}
