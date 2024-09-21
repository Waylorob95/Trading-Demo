package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.config.JwtProvider;
import com.stan.cryptoTrading.modal.TwoFactorAuth;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    //Find user by Jwt
    @Override
    public User findUserByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    //Method for finding the user by his ID
    @Override
    public User findUserById(Long id) {

        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    //Method for enabling the authentication
    @Override
    public User enableOtpAuthentication(User user, VerificationType verificationType, String sentTo) {

        //Check if user already has enabled the authentication
//        if(user.getTwoFactorAuth().equals(true)){
//            throw new RuntimeException("Otp authentication already enabled");
//        }
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSentTo(verificationType);


        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }

    //Method for disabling the authentication
    @Override
    public User disableOtpAuthentication(User user) {

        if(user.getTwoFactorAuth().isEnable()){
            user.getTwoFactorAuth().setEnable(false);
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Otp authentication already disabled");
        }
    }

    //Method for updating the password
    @Override
    public User updatePassword(User user, String password) {
        user.setPassword(password);
        return userRepository.save(user);
    }
}
