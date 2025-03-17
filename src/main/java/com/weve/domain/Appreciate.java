package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appreciate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "worry_id", nullable = false)
    private Worry worry;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 감사편지를 받은 사용자

    private String content;

    private String audioUrl;

    public boolean isRead;
}
