package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.TwoFactorOTP;
import com.stan.cryptoTrading.modal.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);
    TwoFactorOTP findByUser(Long userId);
    TwoFactorOTP findById(String id);
    boolean verifyOtp(TwoFactorOTP twoFactorOTP, String otp);
    void deleteOtp(TwoFactorOTP twoFactorOTP);

}
