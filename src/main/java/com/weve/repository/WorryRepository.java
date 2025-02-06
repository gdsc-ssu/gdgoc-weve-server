package com.weve.repository;

import com.weve.domain.Worry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorryRepository extends JpaRepository<Worry, Long> {
}
