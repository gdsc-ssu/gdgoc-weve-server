package com.weve.repository;

import com.weve.domain.Appreciate;
import com.weve.domain.User;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppreciateRepository extends JpaRepository<Appreciate, Long> {

    List<Appreciate> findByUserAndIsReadIsFalse(User user);

    List<Appreciate> findByUserAndIsReadIsTrue(User user);
}
