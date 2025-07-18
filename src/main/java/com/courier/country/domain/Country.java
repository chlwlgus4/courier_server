package com.courier.country.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "country")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_alpha2", length = 2, unique = true)
    private String isoAlpha2;

    @Column(name = "iso_alpha3", length = 3, unique = true)
    private String isoAlpha3;

    @Column(name = "iso_numeric")
    private Integer isoNumeric;

    @Column(name = "continent_common", length = 50)
    private String continentCommon;

    @Column(name = "continent_admin", length = 50)
    private String continentAdmin;

    @Column(name = "continent_mofa", length = 50)
    private String continentMofa;

    @Column(name = "name_en", length = 100)
    private String nameEn;

    @Column(name = "name_ko", length = 100)
    private String nameKo;

    @Column(name = "calling_code", length = 10)
    private String callingCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_yn", nullable = false)
    private UseYn useYn = UseYn.Y;

    public enum UseYn {
        Y, N
    }



}
