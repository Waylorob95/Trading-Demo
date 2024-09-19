package com.stan.cryptoTrading.repository;

import com.stan.cryptoTrading.modal.TwoFactorOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
        TwoFactorOTP findByUserId(Long userId);
}
