package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(User user, Long amount);
    Withdrawal proceedWithdrawal(Long id, boolean isAccepted) throws Exception;
    List<Withdrawal> getAllUserWithdrawals(User user);

}
