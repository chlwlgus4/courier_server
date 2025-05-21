package com.courier.faq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FaqDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime publishedDate;
    private List<TagDTO> tags;
}
