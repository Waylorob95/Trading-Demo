package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Wallet;
import com.stan.cryptoTrading.modal.Withdrawal;
import com.stan.cryptoTrading.service.UserService;
import com.stan.cryptoTrading.service.WalletService;
import com.stan.cryptoTrading.service.WithdrawalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {
    private final WithdrawalService withdrawalService;

    private final WalletService walletService;

    private final UserService userService;

    public WithdrawalController(WithdrawalService withdrawalService, WalletService walletService, UserService userService) {
        this.withdrawalService = withdrawalService;
        this.walletService = walletService;
        this.userService = userService;
    }

    @PostMapping("/{amount}")
    public ResponseEntity<String> withdrawalRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);
        if(!user.getTwoFactorAuth().isEnable()){
            return new ResponseEntity<>("You must enable TFA in order to withdraw", HttpStatus.FORBIDDEN);
        }

        Wallet wallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(user, amount);
        walletService.addBalance(wallet, -amount);

        return ResponseEntity.ok("Withdrawal request: " + withdrawal.getId() +  " from user " + user.getFullName() + " with ID: " + user.getId() + " was sent successfully");
    }

    @PatchMapping("/{id}/proceed/{isAccepted}")
    public ResponseEntity<String> proceedWithdrawal(@RequestHeader("Authorization") String jwt, @PathVariable Long id, @PathVariable boolean isAccepted) throws Exception {
        User user = userService.findUserByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.proceedWithdrawal(id, isAccepted);

        Wallet wallet = walletService.getUserWallet(user);

        if(!isAccepted){
            walletService.addBalance(wallet, withdrawal.getAmount());
        }
        return ResponseEntity.ok("Withdrawal request with ID: " + id + " was " + (isAccepted ? "accepted" : "rejected"));
    }

    @GetMapping
    public ResponseEntity<List<Withdrawal>> getAllUserWithdrawals(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserByJwt(jwt);

        return ResponseEntity.ok(withdrawalService.getAllUserWithdrawals(user));
    }

}
