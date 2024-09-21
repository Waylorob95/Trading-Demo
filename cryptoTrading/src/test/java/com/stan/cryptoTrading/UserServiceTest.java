package com.stan.cryptoTrading;

import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.modal.TwoFactorAuth;
import com.stan.cryptoTrading.repository.UserRepository;
import com.stan.cryptoTrading.config.JwtProvider;
import com.stan.cryptoTrading.modal.enums.VerificationType;
import com.stan.cryptoTrading.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testFindUserByJwt_Success() {
        String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjY3NTE3ODMsImV4cCI6MTcyNjgzODE4MywiRU1BSUwiOiJzdGFuLnRvZG9yb3YwOUBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6IiJ9.ADSFf0ytIbE1TyIQ-UHGnxUnTHCUxNYdGGF1kfBr6_9Q5oXgWRuEMXfQlE0wAe-_LDBtQGvIdfZ85F7JZdW-eg";
        String email = "stan.todorov09@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("user123");
        when(userRepository.findByEmail(email)).thenReturn(user);


        User result = userService.findUserByJwt(jwt);
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindUserByJwt_UserNotFound() {

        String jwt = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3MjY3NTE3ODMsImV4cCI6MTcyNjgzODE4MywiRU1BSUwiOiJzdGFuLnRvZG9yb3YwOUBnbWFpbC5jb20iLCJhdXRob3JpdGllcyI6IiJ9.ADSFf0ytIbE1TyIQ-UHGnxUnTHCUxNYdGGF1kfBr6_9Q5oXgWRuEMXfQlE0wAe-_LDBtQGvIdfZ85F7JZdW-eg";
        String email = "waylorob@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(null);


        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByJwt(jwt));
    }

    @Test
    void testFindUserByEmail_Success() {

        String email = "krisi_002@abv.bg";
        User user = new User();
        user.setEmail(email);
        user.setPassword("user123");
        when(userRepository.findByEmail(email)).thenReturn(user);


        User result = userService.findUserByEmail(email);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindUserByEmail_UserNotFound() {
        String email = "unknown@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserByEmail(email));
    }

    @Test
    void testFindUserById_Success() {
        Long id = 752L;
        User user = new User();
        user.setEmail("stan.todorov09@gmail.com");
        user.setPassword("user123");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.findUserById(id);

        assertNotNull(result);
        assertEquals("stan.todorov09@gmail.com", result.getEmail());
    }

    @Test
    void testFindUserById_UserNotFound() {
        Long id = 752L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findUserById(id));
    }

    @Test
    void testEnableOtpAuthentication_Success() {
        User user = new User();
        user.setEmail("stan.todorov09@gmail.com");
        user.setPassword("user123");
        VerificationType verificationType = VerificationType.EMAIL;
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.enableOtpAuthentication(user, verificationType, "stan.todorov09@gmail.com");
        assertTrue(result.getTwoFactorAuth().isEnable());
        assertEquals(verificationType, result.getTwoFactorAuth().getSentTo());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDisableOtpAuthentication_Success() {
        User user = new User();
        user.setEmail("stan.todorov09@gmail.com");
        user.setPassword("user123");
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnable(true);
        twoFactorAuth.setSentTo(VerificationType.EMAIL);
        user.setTwoFactorAuth(twoFactorAuth);
        when(userRepository.save(user)).thenReturn(user);


        User result = userService.disableOtpAuthentication(user);
        assertFalse(result.getTwoFactorAuth().isEnable());
        verify(userRepository, times(1)).save(user);
    }
}
