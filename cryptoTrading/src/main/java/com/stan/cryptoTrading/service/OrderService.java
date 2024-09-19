package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.*;
import com.stan.cryptoTrading.modal.enums.OrderType;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long id) throws Exception;
    List<Order> getOrdersOfUser(Long userId, OrderType orderType, String asset);
    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

    OrderItem createOrderItem(Coin coin, double quantity, double price);
}
