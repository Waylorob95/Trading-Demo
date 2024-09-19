package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.PaymentDetails;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.service.PaymentDetailsService;
import com.stan.cryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentDetailsController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<String> addPaymentDetails(@RequestHeader("Authorization")String jwt, @RequestBody PaymentDetails paymentDetails){
        User user = userService.findUserByJwt(jwt);

        PaymentDetails paymentDetails1 = paymentDetailsService.addPaymentDetails(
                paymentDetails.getAccountNumber(),
                paymentDetails.getBankName(),
                paymentDetails.getCvv(),
                user
        );

        return ResponseEntity.ok("Payment details added successfully for user: " + paymentDetails1.getUser().getEmail());
    }

    @GetMapping
    public ResponseEntity<PaymentDetails> getPaymentDetails(@RequestHeader("Authorization")String jwt){
        User user = userService.findUserByJwt(jwt);

        return ResponseEntity.ok(paymentDetailsService.getPaymentDetails(user));
    }
}
