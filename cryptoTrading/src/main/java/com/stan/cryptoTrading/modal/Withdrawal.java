package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Withdrawal {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private WithdrawalStatus status;

   private Long amount;

   @ManyToOne
   private User user;

   private LocalDateTime createdAt = LocalDateTime.now();

}
