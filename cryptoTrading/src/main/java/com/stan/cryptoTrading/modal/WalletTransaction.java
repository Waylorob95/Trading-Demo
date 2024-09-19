package com.stan.cryptoTrading.modal;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;
    @Column(nullable = false)
    private com.stan.cryptoTrading.modal.enums.TransactionType TransactionType;
    private LocalDate localDate;
    @Column(nullable = false)
    private Long amount;
    private String transferId;


}
