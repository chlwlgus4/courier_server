package com.courier.country;

import com.courier.country.dto.CountryAllResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/country")
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/all")
    public ResponseEntity<List<CountryAllResponse>> getCountryAll() {
        List<CountryAllResponse> list = countryService.getCountryAll();
        return ResponseEntity.ok(list);
    }
}
