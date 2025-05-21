package com.courier.faq.repository;

import com.courier.faq.domain.Faq;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FaqRepository extends JpaRepository<Faq, Long> {

    @Query("select f from Faq f join f.tags t " +
            "where t.slug = :slug " +
            "and f.active = true " +
            "order by f.publishedDate desc")
    List<Faq> findAllByTagSlug(@Param("slug") String slug);

    List<Faq> findAllByActiveTrueOrderByPublishedDateDesc();
}
