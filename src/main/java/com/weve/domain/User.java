package com.weve.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weve.domain.common.BaseEntity;
import com.weve.domain.enums.Language;
import com.weve.domain.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 요청 시 JSON → Java 변환
    private LocalDate birth;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private Language language;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "junior", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Worry> worries;

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @Embedded
    private MatchingInfo matchingInfo;
}