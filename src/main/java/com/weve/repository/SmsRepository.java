package com.weve.repository;

import com.weve.domain.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    Optional<Sms> findByUserPhoneNumber(String phoneNumber);

}
