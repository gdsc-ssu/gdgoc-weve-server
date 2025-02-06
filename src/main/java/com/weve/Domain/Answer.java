package com.weve.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "senior_id", nullable = false)
    private User senior;

    @ManyToOne
    @JoinColumn(name = "worry_id", nullable = false)
    private Worry worry;

    private String answer;

    private String letterImageUrl;
}
