package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.Order;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Wallet;

public interface WalletService {
    Wallet createWallet(User user);
    Wallet getUserWallet(User user);
    Wallet findWalletById(Long id) throws Exception;
    Wallet addBalance(Wallet wallet, Long amount);
    Wallet transferToWallet(User sender, Wallet receiverWallet, Long amount ) throws Exception;
    Wallet payOrderPayment(Order order, User user);
}
