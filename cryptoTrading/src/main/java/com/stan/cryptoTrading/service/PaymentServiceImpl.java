package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.PaymentDetails;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.repository.PaymentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentDetailsService{

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String bankName, String cvv, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setHolderName(user.getFullName());
        paymentDetails.setCvv(cvv);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);

        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
