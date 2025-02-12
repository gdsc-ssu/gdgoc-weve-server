package com.weve.repository;

import com.weve.domain.Worry;
import com.weve.domain.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    // 3개 조건 모두 일치
    List<Worry> findByCategoryMapping_JobAndCategoryMapping_ValueAndCategoryMapping_HardshipAndCategory(
            JobCategory job, ValueCategory value, HardshipCategory hardship, WorryCategory category);

    // 2개 조건: job, value
    List<Worry> findByCategoryMapping_JobAndCategoryMapping_ValueAndCategory(
            JobCategory job, ValueCategory value, WorryCategory category);

    // 2개 조건: job, hardship
    List<Worry> findByCategoryMapping_JobAndCategoryMapping_HardshipAndCategory(
            JobCategory job, HardshipCategory hardship, WorryCategory category);

    // 2개 조건: value, hardship
    List<Worry> findByCategoryMapping_ValueAndCategoryMapping_HardshipAndCategory(
            ValueCategory value, HardshipCategory hardship, WorryCategory category);

    // 1개 조건: job
    List<Worry> findByCategoryMapping_JobAndCategory(
            JobCategory job, WorryCategory category);

    // 1개 조건: value
    List<Worry> findByCategoryMapping_ValueAndCategory(
            ValueCategory value, WorryCategory category);

    // 1개 조건: hardship
    List<Worry> findByCategoryMapping_HardshipAndCategory(
            HardshipCategory hardship, WorryCategory category);

    // 0개 조건
    List<Worry> findByCategory(WorryCategory category);
}
