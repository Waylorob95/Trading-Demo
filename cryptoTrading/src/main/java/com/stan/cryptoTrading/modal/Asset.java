package com.stan.cryptoTrading.modal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double quantity;
    private double price;

    @ManyToOne
    private Coin coin;

    @ManyToOne
    private User user;

}
