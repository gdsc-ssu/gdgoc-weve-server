package com.weve.Domain;

import com.weve.Domain.Enum.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
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
