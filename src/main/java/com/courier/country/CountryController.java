package com.courier.country;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    @GetMapping("/all")
    public ResponseEntity<?> getCountryAll() {
        return ResponseEntity.ok().build();
    }
}
