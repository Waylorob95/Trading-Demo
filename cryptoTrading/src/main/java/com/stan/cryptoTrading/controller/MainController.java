package com.stan.cryptoTrading.controller;

import com.stan.cryptoTrading.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String home(){
        return "Welcome to our trading platform";
    }

    @GetMapping("/api")
    public String gase(){
        return "Welcome to our security platform";
    }
}
