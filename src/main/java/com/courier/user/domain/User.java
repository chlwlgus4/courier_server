package com.courier.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(length = 50)
    @ColumnDefault("'ROLE_USER'")
    private String role;

    @Column(unique = true)
    private String email;

    private String phone;

    // OAuth 관련 필드
    private String provider;        // 제공자 (google, naver, kakao 등)
    private String providerId;      // OAuth 제공자의 고유 ID
    private String profileImage;    // 프로필 이미지 URL

}
