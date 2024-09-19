package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.modal.VerificationCode;

public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCode(VerificationCode verificationCode);



}
