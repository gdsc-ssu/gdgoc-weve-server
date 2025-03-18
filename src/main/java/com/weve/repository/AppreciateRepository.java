package com.weve.repository;

import com.weve.domain.Appreciate;
import com.weve.domain.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppreciateRepository extends JpaRepository<Appreciate, Long> {

    List<Appreciate> findByWorry_Answer_SeniorAndIsReadIsFalse(User user); // 읽지 않은 감사편지

    List<Appreciate> findByWorry_Answer_SeniorAndIsReadIsTrue(User user); // 읽은 감사편지
}
