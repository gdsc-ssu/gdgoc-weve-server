package com.weve.Domain;

import com.weve.Domain.Enum.WorryCategory;
import com.weve.Domain.Enum.WorryStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Worry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "junior_id", nullable = false)
    private User junior; // 고민을 작성한 청년 User

    private String content;

    @Enumerated(EnumType.STRING)
    private WorryCategory category;

    @Enumerated(EnumType.STRING)
    private WorryStatus status;

    private LocalDateTime createdAt;

    private boolean isAnonymous;

    private String mp4;

    @OneToMany(mappedBy = "worry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "worry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appreciate> appreciates;
}

