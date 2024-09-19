package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.Order;
import com.stan.cryptoTrading.modal.enums.OrderType;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Wallet;
import com.stan.cryptoTrading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet createWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        walletRepository.save(wallet);

        return wallet;
    }

    //Method to get wallet of a user by his ID
    @Override
    public Wallet getUserWallet(User user) {
        Wallet userWallet = walletRepository.findByUserId(user.getId());
        if(userWallet == null){
            userWallet = new Wallet();
            userWallet.setUser(user);
            walletRepository.save(userWallet);
        }
        return userWallet;
    }

    //Method to find wallet by its ID
    @Override
    public Wallet findWalletById(Long id) throws Exception {

        return walletRepository.findById(id).orElseThrow(() -> new Exception("Wallet not found"));
        }


    //Method to add amount to a wallet balance
    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    //Method for transfer between wallets
    @Override
    public Wallet transferToWallet(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0){
            throw new Exception("Balance not enough for transaction");
        }
        //Update the sender wallet balance
        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);
        //Update the receiver wallet balance
        BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(receiverBalance);
        walletRepository.save(receiverWallet);

        return senderWallet;
    }

    //Method to pay the order payment
    @Override
    public Wallet payOrderPayment(Order order, User user) {
        Wallet wallet = getUserWallet(user);

        //Check the order type
        if(order.getOrderType().equals(OrderType.BUY)){
            //Check if the wallet has enough amount
            if(wallet.getBalance().compareTo(order.getAmount()) < 0){
                throw new RuntimeException("Balance not enough for transaction");
            }
            wallet.setBalance(wallet.getBalance().subtract(order.getAmount()));
        } else {
            wallet.setBalance(wallet.getBalance().add(order.getAmount()));
        }

        return walletRepository.save(wallet);
    }
}
