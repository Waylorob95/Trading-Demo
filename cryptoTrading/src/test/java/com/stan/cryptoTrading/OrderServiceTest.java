package com.stan.cryptoTrading;

import com.stan.cryptoTrading.modal.enums.OrderType;
import com.stan.cryptoTrading.modal.enums.OrderStatus;
import com.stan.cryptoTrading.modal.*;
import com.stan.cryptoTrading.repository.OrderRepository;
import com.stan.cryptoTrading.repository.OrderItemRepository;
import com.stan.cryptoTrading.service.AssetServiceImpl;
import com.stan.cryptoTrading.service.OrderServiceImpl;
import com.stan.cryptoTrading.service.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private WalletServiceImpl walletService;

    @Mock
    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateOrder() {
        User user = new User();
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(new Coin());
        orderItem.setQuantity(1.5);
        orderItem.setPrice(200);

        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        OrderType orderType = OrderType.BUY;

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setAmount(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        when(orderRepository.save(any(Order.class))).thenReturn(order);


        Order result = orderService.createOrder(user, orderItem, orderType);


        assertEquals(user, result.getUser());
        assertEquals(OrderStatus.PENDING, result.getOrderStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderItem() {
        Coin coin = new Coin();
        coin.setCurrentPrice(500);
        double quantity = 1.5;
        double price = coin.getCurrentPrice() * quantity;

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setPrice(price);

        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem result = orderService.createOrderItem(coin, quantity, price);


        assertEquals(quantity, result.getQuantity());
        assertEquals(price, result.getPrice());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testBuyAsset_Success() throws Exception {
        User user = new User();
        Coin coin = new Coin();
        coin.setId("solana");
        coin.setCurrentPrice(100);
        double quantity = 2;

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(OrderType.BUY);
        order.setAmount(BigDecimal.valueOf(coin.getCurrentPrice() * quantity));

        Asset asset = new Asset();
        asset.setCoin(coin);
        asset.setQuantity(0);


        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(assetService.getAssetByUserIdAndCoinId(user.getId(), coin.getId())).thenReturn(asset);
        when(walletService.payOrderPayment(any(Order.class), any(User.class))).thenReturn(new Wallet());


        Order result = orderService.buyAsset(coin, quantity, user);


        assertEquals(OrderStatus.COMPLETED, result.getOrderStatus());
        verify(walletService, times(1)).payOrderPayment(any(Order.class), eq(user));
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testBuyAsset_InsufficientQuantity() {
        Coin coin = new Coin();
        double quantity = -1;


        Exception exception = assertThrows(Exception.class, () -> orderService.buyAsset(coin, quantity, new User()));
        assertEquals("Quantity should be greater than 0", exception.getMessage());
    }

    @Test
    void testSellAsset_Success() throws Exception {
        User user = new User();
        Coin coin = new Coin();
        coin.setId("sol");
        coin.setCurrentPrice(100);
        double quantity = 1.5;

        Asset asset = new Asset();
        asset.setQuantity(2);

        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(OrderType.SELL);


        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(assetService.getAssetByUserIdAndCoinId(user.getId(), coin.getId())).thenReturn(asset);
        when(walletService.payOrderPayment(any(Order.class), any(User.class))).thenReturn(new Wallet());

        Order result = orderService.sellAsset(coin, quantity, user);


        assertEquals(OrderStatus.COMPLETED, result.getOrderStatus());
        verify(walletService, times(1)).payOrderPayment(any(Order.class), eq(user));
        verify(assetService, times(1)).updateAsset(asset.getId(), -quantity);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testProcessOrder_BuyOrder() throws Exception {
        User user = new User();
        user.setId(1L);
        Coin coin = new Coin();
        coin.setId("solana");
        double quantity = 1.5;
        OrderType orderType = OrderType.BUY;

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);


        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(walletService.payOrderPayment(any(Order.class), eq(user))).thenReturn(new Wallet());
        when(assetService.getAssetByUserIdAndCoinId(anyLong(), anyString())).thenReturn(null);
        when(assetService.createAsset(any(User.class), any(Coin.class), eq(quantity))).thenReturn(new Asset());


        Order result = orderService.processOrder(coin, quantity, orderType, user);


        assertEquals(order, result);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testProcessOrder_SellOrder() throws Exception {
        User user = new User();
        user.setId(1L);
        Coin coin = new Coin();
        coin.setId("solana");
        double quantity = 1.5;
        OrderType orderType = OrderType.SELL;

        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);

        Asset asset = new Asset();
        asset.setQuantity(5);


        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(walletService.payOrderPayment(any(Order.class), eq(user))).thenReturn(new Wallet());
        when(assetService.getAssetByUserIdAndCoinId(anyLong(), anyString())).thenReturn(asset);
        when(assetService.updateAsset(anyLong(), eq(-quantity))).thenReturn(asset);


        Order result = orderService.processOrder(coin, quantity, orderType, user);


        assertEquals(order, result);
        verify(orderRepository, times(2)).save(any(Order.class));
    }

    @Test
    void testGetOrderById_Success() throws Exception {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));


        Order result = orderService.getOrderById(orderId);


        assertNotNull(result);
        assertEquals(orderId, result.getId());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        Long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());


        Exception exception = assertThrows(Exception.class, () -> orderService.getOrderById(orderId));
        assertEquals("Order not found", exception.getMessage());
    }
}

