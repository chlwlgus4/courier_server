package com.courier.hstariff;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/hs-tariff")
@RestController
@RequiredArgsConstructor
public class HsTariffController {

    private final HsTariffService service;

    @PostMapping("/import")
    public String importData() {
        String tariffPath = "hscode/tariffFile.xlsx";
        String hsCodePath = "hscode/hsCode.xlsx";

        service.importData(tariffPath, hsCodePath);
        return "Import 완료!";
    }


}
