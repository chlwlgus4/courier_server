package com.courier.util;

import com.courier.hstariff.dto.HsTariffDTO;
import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExcelImportService {

    @PostConstruct
    public void init() {
        IOUtils.setByteArrayMaxOverride(200_000_000);
    }

    public Map<String, HsTariffDTO> parseTariffExcel(String filePath) {
        Map<String, HsTariffDTO> tariffMap = new HashMap<>();
        try (InputStream fis = new ClassPathResource(filePath).getInputStream();
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String hsCode = getCellStringValue(row.getCell(0));
                BigDecimal dutyRate = getCellNumericValue(row.getCell(2));
                LocalDate startDate = parseDateValue(getCellStringValue(row.getCell(7)));
                LocalDate endDate = parseDateValue(getCellStringValue(row.getCell(8)));
                BigDecimal unitTax =  getCellNumericValue(row.getCell(3));
                BigDecimal referencePrice = getCellNumericValue(row.getCell(4));
                String useTypeCode = getCellStringValue(row.getCell(5));

                HsTariffDTO dto = HsTariffDTO.builder()
                        .hsCode(hsCode)
                        .dutyRate(dutyRate)
                        .startDate(startDate)
                        .endDate(endDate)
                        .unitTax(unitTax)
                        .referencePrice(referencePrice)
                        .useTypeCode(useTypeCode)
                        .build();
                tariffMap.put(hsCode, dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tariffMap;
    }

    public Map<String, HsTariffDTO> parseHsCodeExcel(String filePath) {
        Map<String, HsTariffDTO> hsMap = new HashMap<>();
        try (InputStream fis = new ClassPathResource(filePath).getInputStream();
             Workbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String hsCode = getCellStringValue(row.getCell(0));
                String koreanName = getCellStringValue(row.getCell(3));
                String englishName = getCellStringValue(row.getCell(4));
                String notes = getCellStringValue(row.getCell(19));

                HsTariffDTO dto = HsTariffDTO.builder()
                        .hsCode(hsCode)
                        .koreanName(koreanName)
                        .englishName(englishName)
                        .notes(notes)
                        .build();

                hsMap.put(hsCode, dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hsMap;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // 숫자를 문자열로 변환
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return cell.getStringCellValue();
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return "";
                    }
                }
            default:
                return "";
        }
    }

    private BigDecimal getCellNumericValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    // 문자열을 숫자로 변환 시도
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            case FORMULA:
                try {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    try {
                        return new BigDecimal(cell.getStringCellValue());
                    } catch (Exception ex) {
                        return null;
                    }
                }
            default:
                return null;
        }
    }

    private LocalDate parseDateValue(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.BASIC_ISO_DATE);
        } catch (Exception e) {
            // 다른 날짜 형식을 시도해볼 수 있음
            return null;
        }
    }

}


