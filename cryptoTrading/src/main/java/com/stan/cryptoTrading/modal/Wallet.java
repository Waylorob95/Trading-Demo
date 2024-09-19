package com.stan.cryptoTrading.modal;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private BigDecimal balance = BigDecimal.valueOf(0.0);

    @OneToOne
    private User user;

}
