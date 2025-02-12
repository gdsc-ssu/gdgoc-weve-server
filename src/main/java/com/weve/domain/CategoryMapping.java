package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "worry_id")
    private Worry worry;

    @OneToOne
    @JoinColumn(name = "senior_id")
    private User senior;

    @Enumerated(EnumType.STRING)
    private JobCategory job;

    @Enumerated(EnumType.STRING)
    private ValueCategory value;

    @Enumerated(EnumType.STRING)
    private HardshipCategory hardship;
}
