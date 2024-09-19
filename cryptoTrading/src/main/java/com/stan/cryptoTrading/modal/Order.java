package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.OrderStatus;
import com.stan.cryptoTrading.modal.enums.OrderType;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private OrderType orderType;
    @Column(nullable = false)
    private OrderStatus orderStatus;
    @Column(nullable = false)
    private BigDecimal amount;
    private LocalDateTime timestamp = LocalDateTime.now();
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private OrderItem orderItem;


}
