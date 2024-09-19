package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.Order;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Wallet;
import com.stan.cryptoTrading.modal.WalletTransaction;
import com.stan.cryptoTrading.service.OrderService;
import com.stan.cryptoTrading.service.UserServiceImpl;
import com.stan.cryptoTrading.service.WalletServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wallet")
public class WalletController {

    @Autowired
    private WalletServiceImpl walletService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderService orderService;


    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("{walletId}/transfer")
    public ResponseEntity<String> transferToWallet(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable long walletId,
                                                   @RequestBody WalletTransaction walletTransaction) throws Exception {
        User sender = userService.findUserByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet senderWallet = walletService.transferToWallet(sender, receiverWallet, walletTransaction.getAmount());

        return new ResponseEntity<>("You have send " + walletTransaction.getAmount() + ". You are left with " + senderWallet.getBalance(), HttpStatus.OK);
    }

    @PutMapping("{walletId}/deposit")
    public ResponseEntity<String> depositToWallet(@RequestHeader("Authorization") String jwt,
                                                  @PathVariable long walletId,
                                                   @RequestParam Long amount) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Wallet wallet = walletService.findWalletById(walletId);

        walletService.addBalance(wallet, amount);


        return new ResponseEntity<>("You have deposited " + amount + ". You have:  " + wallet.getBalance(), HttpStatus.OK);
    }

    @PutMapping("api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                                  @PathVariable long orderId) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, user);


       return new ResponseEntity<>(wallet, HttpStatus.OK);

    }
}
