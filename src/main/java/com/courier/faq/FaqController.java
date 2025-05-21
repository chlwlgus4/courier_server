package com.courier.faq;

import com.courier.faq.dto.FaqDTO;
import com.courier.faq.dto.TagDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    /**
     * 모든 태그 목록
     * @return list
     */
    @GetMapping("/tags")
    public List<TagDTO> getTags() {
        return faqService.getAllTags();
    }


    /**
     * 특정 태그(slug) 또는 전체("all")에 해당하는 FAQ 목록
     * @param tag
     * @return list
     */
    @GetMapping
    public List<FaqDTO> getFaqs(@RequestParam(defaultValue = "all") String tag) {
        return faqService.getFaqsByTag(tag);
    }

}
