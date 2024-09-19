package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.PaymentOrderStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;
    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    @ManyToOne
    private User user;
}
