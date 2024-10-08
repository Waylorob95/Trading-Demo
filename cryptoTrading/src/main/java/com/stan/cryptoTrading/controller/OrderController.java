package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.modal.*;
import com.stan.cryptoTrading.modal.enums.OrderType;
import com.stan.cryptoTrading.service.CoinService;
import com.stan.cryptoTrading.service.OrderService;
import com.stan.cryptoTrading.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    private final UserService userService;

    private final CoinService coinService;

    public OrderController(OrderService orderService, UserService userService, CoinService coinService) {
        this.orderService = orderService;
        this.userService = userService;
        this.coinService = coinService;
    }


    @PostMapping("/pay")
    public ResponseEntity<?> payOrder(@RequestHeader("Authorization") String jwt, @RequestBody CreateOrderRequest orderRequest) throws Exception {
       User user = userService.findUserByJwt(jwt);

        if(!user.getTwoFactorAuth().isEnable()){
            return new ResponseEntity<>("You have to enable TFA in order to proceed the order", HttpStatus.FORBIDDEN);
        }

       Coin coin = coinService.getCoinById(orderRequest.getCoinId());

       Order order = orderService.processOrder(coin, orderRequest.getQuantity(), orderRequest.getOrderType(), user);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId ) throws Exception {

        User user = userService.findUserByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        if(order.getUser().getId().equals(user.getId())){
            return new ResponseEntity<>(order, HttpStatus.FOUND);
        } else {
            throw new Exception("Unauthorized access");
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllUserOrders(@RequestHeader("Authorization") String jwt,
                                                        @RequestParam(required = false) String orderType,
                                                        @RequestParam(required = false) String assetSymbol){

        User user = userService.findUserByJwt(jwt);
        List<Order> userOrders = orderService.getOrdersOfUser(user.getId(), OrderType.valueOf(orderType.toUpperCase()), assetSymbol);


        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }


}
