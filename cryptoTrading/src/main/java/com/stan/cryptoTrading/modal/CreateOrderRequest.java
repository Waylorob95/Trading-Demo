package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
