package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import com.weve.domain.enums.Language;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Date birth;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private Language language;

    private String phoneNumber;

    private boolean isSenior;

    @OneToMany(mappedBy = "junior", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Worry> worries;

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "senior", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CategoryMapping> categoryMappings;
}
