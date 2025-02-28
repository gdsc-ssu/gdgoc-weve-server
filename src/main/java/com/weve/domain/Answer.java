package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private User senior;

    @OneToOne
    @JoinColumn(name = "worry_id", nullable = false)
    private Worry worry;

    private String content;

    private String imageUrl;

    private String audioUrl;
}
