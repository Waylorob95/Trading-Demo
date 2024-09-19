package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.PaymentDetails;
import com.stan.cryptoTrading.modal.User;

public interface PaymentDetailsService {

    PaymentDetails addPaymentDetails(String accountNumber, String holderName, String cvv, User user);
    PaymentDetails getPaymentDetails(User user);

}
