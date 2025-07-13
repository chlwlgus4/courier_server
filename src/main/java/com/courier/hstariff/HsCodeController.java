package com.courier.hstariff;

import com.courier.hstariff.dto.HsCodeSuggestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/hs-code")
@RestController
@RequiredArgsConstructor
public class HsCodeController {

    private final HsCodeService service;

    @PostMapping("/import")
    public String importData() {
        String tariffPath = "hscode/tariffFile.xlsx";
        String hsCodePath = "hscode/hsCode.xlsx";

        service.importData(tariffPath, hsCodePath);
        return "Import 완료!";
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<HsCodeSuggestionResponse>> suggestHsCodes(@RequestParam String keyword) {
        List<HsCodeSuggestionResponse> list = service.suggestHsCodes(keyword);
        return ResponseEntity.ok(list);
    }

}
