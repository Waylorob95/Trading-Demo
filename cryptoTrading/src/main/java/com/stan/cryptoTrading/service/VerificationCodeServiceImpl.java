package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.modal.VerificationCode;
import com.stan.cryptoTrading.repository.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.stan.cryptoTrading.utils.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationRepository verificationRepository;

    //Method for sending verification code
    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(OtpUtils.generateOtp());
        verificationCode.setSentTo(verificationType);
        verificationCode.setUser(user);
        verificationCode.setEmail(user.getEmail());


        return verificationRepository.save(verificationCode);
    }

    //Method for getting verification code by its ID
    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {

        return verificationRepository.findById(id).orElseThrow(() -> new Exception("Verification code not found"));
    }

    //Method for getting the verification code by user ID
    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationRepository.delete(verificationCode);
    }

}
