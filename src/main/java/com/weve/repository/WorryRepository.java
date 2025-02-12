package com.weve.repository;

import com.weve.domain.Worry;
import com.weve.domain.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorryRepository extends JpaRepository<Worry, Long> {

    // 3개 조건 모두 일치
    List<Worry> findByMatchingInfo_JobAndMatchingInfo_ValueAndMatchingInfo_HardshipAndCategory(
            JobCategory job, ValueCategory value, HardshipCategory hardship, WorryCategory category);

    // 2개 조건: job, value
    List<Worry> findByMatchingInfo_JobAndMatchingInfo_ValueAndCategory(
            JobCategory job, ValueCategory value, WorryCategory category);

    // 2개 조건: job, hardship
    List<Worry> findByMatchingInfo_JobAndMatchingInfo_HardshipAndCategory(
            JobCategory job, HardshipCategory hardship, WorryCategory category);

    // 2개 조건: value, hardship
    List<Worry> findByMatchingInfo_ValueAndMatchingInfo_HardshipAndCategory(
            ValueCategory value, HardshipCategory hardship, WorryCategory category);

    // 1개 조건: job
    List<Worry> findByMatchingInfo_JobAndCategory(
            JobCategory job, WorryCategory category);

    // 1개 조건: value
    List<Worry> findByMatchingInfo_ValueAndCategory(
            ValueCategory value, WorryCategory category);

    // 1개 조건: hardship
    List<Worry> findByMatchingInfo_HardshipAndCategory(
            HardshipCategory hardship, WorryCategory category);

    // 0개 조건
    List<Worry> findByCategory(WorryCategory category);
}
