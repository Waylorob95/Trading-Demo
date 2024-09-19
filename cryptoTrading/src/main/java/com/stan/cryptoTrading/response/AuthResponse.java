package com.stan.cryptoTrading.response;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwtToken;
    private boolean status;
    private String message;
    private boolean isTwoFactorEnabled;
    private String session;
}