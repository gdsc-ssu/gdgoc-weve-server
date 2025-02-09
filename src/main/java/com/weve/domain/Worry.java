package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import com.weve.domain.enums.WorryCategory;
import com.weve.domain.enums.WorryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Worry extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "junior_id", nullable = false)
    private User junior; // 고민을 작성한 청년 User

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private WorryCategory category;

    @Enumerated(EnumType.STRING)
    private WorryStatus status;

    private boolean isAnonymous;

    private String mp4;

    @OneToMany(mappedBy = "worry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Answer> answers;

    @OneToMany(mappedBy = "worry", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appreciate> appreciates;
}

