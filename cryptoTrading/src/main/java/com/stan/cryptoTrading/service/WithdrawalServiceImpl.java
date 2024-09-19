package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Withdrawal;
import com.stan.cryptoTrading.modal.enums.WithdrawalStatus;
import com.stan.cryptoTrading.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WithdrawalServiceImpl implements WithdrawalService{

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(User user, Long amount) {
        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setUser(user);
        withdrawal.setAmount(amount);
        withdrawal.setStatus(WithdrawalStatus.PENDING);

        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal proceedWithdrawal(Long id, boolean isAccepted) throws Exception {
        Withdrawal withdrawal = withdrawalRepository.findById(id).orElseThrow(() -> new Exception("Withdrawal not found"));
        withdrawal.setCreatedAt(LocalDateTime.now());

        if(isAccepted){
            withdrawal.setStatus(WithdrawalStatus.COMPLETED);
        } else {
            withdrawal.setStatus(WithdrawalStatus.FAILED);
        }
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getAllUserWithdrawals(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }
}
