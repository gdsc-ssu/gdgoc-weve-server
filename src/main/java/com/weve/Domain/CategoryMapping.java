package com.weve.Domain;

import com.weve.Domain.Enum.HardshipCategory;
import com.weve.Domain.Enum.JobCategory;
import com.weve.Domain.Enum.ValueCategory;
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
