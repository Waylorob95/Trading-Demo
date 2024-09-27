package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.*;
import com.stan.cryptoTrading.modal.enums.OrderStatus;
import com.stan.cryptoTrading.modal.enums.OrderType;
import com.stan.cryptoTrading.repository.OrderItemRepository;
import com.stan.cryptoTrading.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AssetService assetService;
    private final WalletService walletService;

    public OrderServiceImpl(OrderRepository orderRepository, WalletService walletService, OrderItemRepository orderItemRepository, AssetService assetService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.assetService = assetService;
        this.walletService = walletService;
    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setAmount(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public OrderItem createOrderItem(Coin coin, double quantity, double price) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);

        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("Quantity should be greater than 0");
        }

        OrderItem orderItem = createOrderItem(coin, quantity, coin.getCurrentPrice());
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order, user);
        order.setOrderStatus(OrderStatus.COMPLETED);

        Asset asset = assetService.getAssetByUserIdAndCoinId(user.getId(), coin.getId());
        //Check if there is asset and update it or create it if there is none
        if(asset == null){
            assetService.createAsset(user, coin, quantity);
        } else {
            assetService.updateAsset(asset.getId(), quantity);
        }

        return orderRepository.save(order);
    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if(quantity <= 0){
            throw new Exception("Quantity should be greater than 0");
        }

        Asset asset = assetService.getAssetByUserIdAndCoinId(user.getId(), coin.getId());
        //if assetToSell > quantity...proceed
        if(asset != null){
            OrderItem orderItem = createOrderItem(coin, quantity, coin.getCurrentPrice());
            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if(asset.getQuantity() >= quantity){

                walletService.payOrderPayment(order, user);
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);

                assetService.updateAsset(asset.getId(), -quantity);
                if(asset.getQuantity() < 0) {
                    assetService.deleteAsset(asset.getId());
                }
                return order;
            } else {
                throw new Exception("Insufficient quantity of asset");
            }
        }
        throw new Exception("Asset not sold");
    }

    @Override
    public Order getOrderById(Long id) throws Exception {

        return orderRepository.findById(id).orElseThrow(() -> new Exception("Order not found"));
    }

    @Override
    public List<Order> getOrdersOfUser(Long userId, OrderType orderType, String asset) {

        return orderRepository.findByUserId(userId);
    }

    @Transactional
    @Override
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if(orderType != null){
            switch (orderType) {
                case BUY:
                    return buyAsset(coin, quantity, user);
                case SELL:
                    return sellAsset(coin, quantity, user);
                default:
                    throw new Exception("Invalid order type: " + orderType);
            }
        }
        throw new Exception("Order type cannot be null");
    }

}
