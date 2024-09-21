package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.modal.VerificationCode;
import com.stan.cryptoTrading.service.EmailService;
import com.stan.cryptoTrading.service.TwoFactorOtpService;
import com.stan.cryptoTrading.service.UserService;
import com.stan.cryptoTrading.service.VerificationCodeService;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @GetMapping("api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);

        return new ResponseEntity<User>(user, HttpStatus.FOUND);
    }

    @PostMapping("api/users/verification/send-otp")
    public ResponseEntity<String> sendOtp(@RequestHeader("Authorization") String jwt, @RequestParam(name = "verification_type") String verification_type) throws MessagingException {
        // Get user via the token provided
        User user = userService.findUserByJwt(jwt);
        //Search the verification code of the current user
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        VerificationType verificationType1 = VerificationType.valueOf(verification_type.toUpperCase());
        //Check whether there is verification code and what type is it
        if(verificationCode == null){
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType1);
        }
        if(verificationType1.equals(VerificationType.EMAIL)){
            emailService.sendVerification(user.getEmail(), verificationCode.getOtp());
        }


        return new ResponseEntity<>("Verification OTP sent successfully to " + user.getEmail(), HttpStatus.OK);
        }


    @PatchMapping("api/users/enable-tfa/verify-opt/{otp}")
    public ResponseEntity<String> enableTfa(@PathVariable String otp, @RequestHeader("Authorization") String jwt) throws AuthenticationFailedException {
       //Get the user via provided token
        User user = userService.findUserByJwt(jwt);
        //Get the verification code of the current user
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sentTo = verificationCode.getSentTo().equals(VerificationType.EMAIL) ? verificationCode.getEmail() : verificationCode.getMobile();
        //Check if the otp is valid then enable the tfa
        if(verificationCode.getOtp().equals(otp)){
            User updatedUser = userService.enableOtpAuthentication(user, verificationCode.getSentTo(), sentTo);
            twoFactorOtpService.createTwoFactorOtp(user, otp, jwt);
            //delete the old verification code
            verificationCodeService.deleteVerificationCode(verificationCode);

            return new ResponseEntity<>("You have enabled the TFA for user: " + updatedUser.getEmail(), HttpStatus.OK);
        }
        throw new AuthenticationFailedException("Wrong OTP");
    }
}
