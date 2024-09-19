package com.stan.cryptoTrading.repository;

import com.stan.cryptoTrading.modal.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {

    VerificationCode findByUserId(Long userId);

}
