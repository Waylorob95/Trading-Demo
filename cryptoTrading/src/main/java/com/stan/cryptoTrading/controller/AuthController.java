package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.config.JwtProvider;
import com.stan.cryptoTrading.modal.TwoFactorAuth;
import com.stan.cryptoTrading.modal.TwoFactorOTP;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.repository.UserRepository;
import com.stan.cryptoTrading.response.AuthResponse;
import com.stan.cryptoTrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.stan.cryptoTrading.utils.OtpUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpServiceImpl;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WalletService walletService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        //Check if the user is registered with current EMAIL
        User isRegistered = userRepository.findByEmail(user.getEmail());
        if(isRegistered != null){
            throw new Exception("User is already registered");
        }

        //Register user
        User newUser =  new User();
        newUser.setEmail(user.getEmail());
        newUser.setFullName(user.getFullName());
        newUser.setPassword(user.getPassword());
        TwoFactorAuth auth = new TwoFactorAuth();
        auth.setSentTo(VerificationType.EMAIL);
        newUser.setTwoFactorAuth(auth);
        User savedUser = userRepository.save(newUser);

        walletService.createWallet(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //Generate JWT token and set the response
        String jwtToken = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("User is registered successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/singin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        //Authenticate the user
        Authentication authentication = authenticate(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate token
        String jwtToken = JwtProvider.generateToken(authentication);

        User authUser = userRepository.findByEmail(user.getEmail());

        //check if two-factor auth is enabled
        if(authUser.getTwoFactorAuth().isEnable()){
            AuthResponse authResponse = new AuthResponse();
            authResponse.setMessage("Two factor auth is enabled");
            authResponse.setTwoFactorEnabled(true);
            authResponse.setStatus(true);
            String otp = OtpUtils.generateOtp();

            //if there is old otp it will delete it
            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpServiceImpl.findByUser(authUser.getId());
            if(oldTwoFactorOTP != null){
                twoFactorOtpServiceImpl.deleteOtp(oldTwoFactorOTP);
            }

            //otherwise it creates new
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpServiceImpl.createTwoFactorOtp(
                    authUser,
                    otp,
                    jwtToken
            );

            //send EMAIL for verification
            emailService.sendVerification(user.getEmail(), otp);

            authResponse.setSession(newTwoFactorOTP.getId());
            return new ResponseEntity<>(authResponse, HttpStatus.ACCEPTED);
            }

        //Set response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setStatus(true);
        authResponse.setMessage("Singing successfully");

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOtp(
            @PathVariable String otp,
            @RequestParam String id) throws Exception {
     TwoFactorOTP twoFactorOTP = twoFactorOtpServiceImpl.findById(id);
     if(twoFactorOtpServiceImpl.verifyOtp(twoFactorOTP, otp)){
         AuthResponse authResponse = new AuthResponse();
         authResponse.setMessage("Two-factor authentication verified");
         authResponse.setTwoFactorEnabled(true);
         authResponse.setStatus(true);
         authResponse.setSession(id);
         authResponse.setJwtToken(twoFactorOTP.getJwt());

         return new ResponseEntity<>(authResponse, HttpStatus.OK);
     }
     throw new Exception("Invalid Otp");
    }

    // Method for authentication of the user
    private Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserService.loadUserByUsername(email);
        // Check if the user is registered
        if(userDetails.equals(null)){
            throw new BadCredentialsException("Invalid username/EMAIL");
        }
        // Check if the password is correct
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }
}
