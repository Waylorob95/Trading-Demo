package com.stan.cryptoTrading;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.Wallet;
import com.stan.cryptoTrading.modal.Order;
import com.stan.cryptoTrading.modal.enums.OrderType;
import com.stan.cryptoTrading.service.WalletServiceImpl;
import com.stan.cryptoTrading.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceTest {

    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateWallet() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.createWallet(user);
        assertEquals(user, result.getUser());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testGetUserWallet_WhenWalletExists() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        when(walletRepository.findByUserId(user.getId())).thenReturn(wallet);

        Wallet result = walletService.getUserWallet(user);
        assertNotNull(result);
        assertEquals(user.getId(), result.getUser().getId());
        verify(walletRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testGetUserWallet_WhenWalletDoesNotExist() {
        User user = new User();
        user.setId(1L);
        when(walletRepository.findByUserId(user.getId())).thenReturn(null);
        when(walletRepository.save(any(Wallet.class))).thenReturn(new Wallet());

        Wallet result = walletService.getUserWallet(user);
        assertNotNull(result);
        assertEquals(user, result.getUser());
        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

    @Test
    void testFindWalletById_Success() throws Exception {
        Long walletId = 1L;
        Wallet wallet = new Wallet();
        wallet.setId(walletId);
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Wallet result = walletService.findWalletById(walletId);
        assertEquals(walletId, result.getId());
        verify(walletRepository, times(1)).findById(walletId);
    }

    @Test
    void testFindWalletById_NotFound() {
        Long walletId = 1L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(Exception.class, () -> walletService.findWalletById(walletId));
        assertEquals("Wallet not found", exception.getMessage());
    }

    @Test
    void testAddBalance() {
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(1000));
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.addBalance(wallet, 500L);
        assertEquals(BigDecimal.valueOf(1500), result.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testTransferToWallet_Success() throws Exception {
        User sender = new User();
        sender.setId(1L);

        Wallet senderWallet = new Wallet();
        senderWallet.setUser(sender);
        senderWallet.setBalance(BigDecimal.valueOf(1000));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setBalance(BigDecimal.valueOf(500));

        when(walletRepository.findByUserId(sender.getId())).thenReturn(senderWallet);
        when(walletRepository.save(senderWallet)).thenReturn(senderWallet);
        when(walletRepository.save(receiverWallet)).thenReturn(receiverWallet);

        Wallet result = walletService.transferToWallet(sender, receiverWallet, 300L);
        assertEquals(BigDecimal.valueOf(700), result.getBalance());
        assertEquals(BigDecimal.valueOf(800), receiverWallet.getBalance());
        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
    }

    @Test
    void testTransferToWallet_InsufficientBalance() {
        User sender = new User();
        sender.setId(1L);

        Wallet senderWallet = new Wallet();
        senderWallet.setBalance(BigDecimal.valueOf(100));

        Wallet receiverWallet = new Wallet();

        when(walletRepository.findByUserId(sender.getId())).thenReturn(senderWallet);

        Exception exception = assertThrows(Exception.class, () -> walletService.transferToWallet(sender, receiverWallet, 500L));
        assertEquals("Balance not enough for transaction", exception.getMessage());
    }

    @Test
    void testPayOrderPayment_BuyOrder() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(1000));

        Order order = new Order();
        order.setOrderType(OrderType.BUY);
        order.setAmount(BigDecimal.valueOf(500));

        when(walletRepository.findByUserId(user.getId())).thenReturn(wallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.payOrderPayment(order, user);
        assertEquals(BigDecimal.valueOf(500), result.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testPayOrderPayment_SellOrder() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(500));

        Order order = new Order();
        order.setOrderType(OrderType.SELL);
        order.setAmount(BigDecimal.valueOf(200));

        when(walletRepository.findByUserId(user.getId())).thenReturn(wallet);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);

        Wallet result = walletService.payOrderPayment(order, user);

        assertEquals(BigDecimal.valueOf(700), result.getBalance());
        verify(walletRepository, times(1)).save(wallet);
    }
}
