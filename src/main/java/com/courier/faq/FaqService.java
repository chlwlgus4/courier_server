package com.courier.faq;

import com.courier.faq.domain.Faq;
import com.courier.faq.dto.FaqDTO;
import com.courier.faq.dto.TagDTO;
import com.courier.faq.repository.FaqRepository;
import com.courier.faq.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;
    private final TagRepository tagRepository;

    public List<TagDTO> getAllTags() {
        return tagRepository.findAll().stream()
                .map(t -> TagDTO.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .slug(t.getSlug())
                        .build())
                .toList();
    }

    public List<FaqDTO> getFaqsByTag(String slug) {
        // 1단계: 데이터베이스에서 FAQ 목록을 조회
        List<Faq> faqs = "all".equals(slug)
                ? faqRepository.findAllByActiveTrueOrderByPublishedDateDesc()
                : faqRepository.findAllByTagSlug(slug);
        // 2단계 : 스트림을 생성하고 처리
        return faqs.stream()
                .map(this::toDto)
                .toList();
    }

    private FaqDTO toDto(Faq f) {
        //  @Data 사용시
        //  faq.getTags() 호출 → tags 컬렉션 초기화 시작
        //  Tag 객체의 equals/hashCode 호출
        //  tag.getFaqs() 내부 접근
        //  faqs 컬렉션 초기화 시도
        //  이미 초기화 중인 컬렉션 접근
        //  ConcurrentModificationException
        List<TagDTO> tags = f.getTags().stream()
                .map(t -> TagDTO.builder()
                        .id(t.getId())
                        .name(t.getName())
                        .slug(t.getSlug())
                        .build())
                .toList();

        return FaqDTO.builder()
                .id(f.getId())
                .title(f.getTitle())
                .content(f.getContent())
                .publishedDate(f.getPublishedDate())
                .tags(tags)
                .build();
    }

}
