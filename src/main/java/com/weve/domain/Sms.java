package com.weve.domain;

import com.weve.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sms")
public class Sms extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column
    private String smsCode;

    @Column
    private LocalDateTime smsCodeExpiry;

    // smsCode 갱신
    public void updatesmsCode(String code, LocalDateTime expiry) {
        this.smsCode = code;
        this.smsCodeExpiry = expiry;
    }

    // smsCode 초기화
    public void clearSmsCode() {
        this.smsCode = null;
        this.smsCodeExpiry = null;
    }
}
