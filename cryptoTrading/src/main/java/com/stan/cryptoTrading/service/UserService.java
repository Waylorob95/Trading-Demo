package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;

public interface UserService {

     User findUserByJwt(String jwt);
     User findUserByEmail(String email);
     User findUserById(Long id) throws Exception;

     User enableOtpAuthentication(User user, VerificationType verificationType, String sentTo);

     User disableOtpAuthentication(User user);

     User updatePassword(User user, String password);

    }

