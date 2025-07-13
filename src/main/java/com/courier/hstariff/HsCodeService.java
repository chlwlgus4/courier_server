package com.courier.hstariff;

import com.courier.hstariff.domain.HsTariff;
import com.courier.hstariff.dto.HsCodeSuggestionResponse;
import com.courier.hstariff.dto.HsTariffDTO;
import com.courier.hstariff.repository.HsTariffRepository;
import com.courier.util.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HsCodeService {
    private final HsTariffRepository repo;
    private final ExcelImportService excelService;

    private static final int MAX_KOREAN_NAME_LENGTH = 255;
    private static final int MAX_ENGLISH_NAME_LENGTH = 255;

    public void importData(String tariffPath, String hsCodePath) {
        Map<String, HsTariffDTO> tariffMap = excelService.parseTariffExcel(tariffPath);
        Map<String, HsTariffDTO> hsMap = excelService.parseHsCodeExcel(hsCodePath);

        for (String hsCode : tariffMap.keySet()) {
            HsTariffDTO tariffDto = tariffMap.get(hsCode);
            HsTariffDTO hsDto = hsMap.getOrDefault(hsCode, HsTariffDTO.builder().hsCode(hsCode).build());

            HsTariff entity = repo.findByHsCodeAndStartDate(hsCode, tariffDto.getStartDate())
                    .orElse(new HsTariff());
            entity.setHsCode(hsCode);

            String koreanName = hsDto.getKoreanName();
            if (koreanName != null && koreanName.length() > MAX_KOREAN_NAME_LENGTH) {
                koreanName = koreanName.substring(0, MAX_KOREAN_NAME_LENGTH);
            }
            entity.setKoreanName(koreanName);

            String englishName = hsDto.getEnglishName();
            if (englishName != null && englishName.length() > MAX_ENGLISH_NAME_LENGTH) {
                englishName = englishName.substring(0, MAX_ENGLISH_NAME_LENGTH);
            }

            entity.setEnglishName(englishName);
            entity.setNotes(hsDto.getNotes());
            entity.setDutyRate(tariffDto.getDutyRate());
            entity.setStartDate(tariffDto.getStartDate());
            entity.setEndDate(tariffDto.getEndDate());
            entity.setUnitTax(tariffDto.getUnitTax());
            entity.setReferencePrice(tariffDto.getReferencePrice());
            entity.setUseTypeCode(tariffDto.getUseTypeCode());

            repo.save(entity);
        }
    }

    public List<HsCodeSuggestionResponse> suggestHsCodes(String keyword) {
        List<HsTariff> list = repo.findTop10ByKoreanNameContainingIgnoreCaseOrEnglishNameContainingIgnoreCase(keyword, keyword);

        return list.stream().map(hc -> HsCodeSuggestionResponse.builder()
                .hsCode(hc.getHsCode())
                .koreanName(hc.getKoreanName())
                .englishName(hc.getEnglishName())
                .build()
        ).toList();
    }

}


