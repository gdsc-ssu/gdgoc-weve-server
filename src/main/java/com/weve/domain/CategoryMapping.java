package com.weve.domain;

import com.weve.domain.enums.HardshipCategory;
import com.weve.domain.enums.JobCategory;
import com.weve.domain.enums.ValueCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CategoryMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "worry_id", nullable = false)
    private Worry worry;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private User senior;

    @Enumerated(EnumType.STRING)
    private JobCategory job;

    @Enumerated(EnumType.STRING)
    private ValueCategory value;

    @Enumerated(EnumType.STRING)
    private HardshipCategory hardship;
}
