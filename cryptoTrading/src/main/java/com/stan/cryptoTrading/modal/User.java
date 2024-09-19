package com.stan.cryptoTrading.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stan.cryptoTrading.modal.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserRole role = UserRole.CUSTOMER;

    @Embedded
    private TwoFactorAuth twoFactorAuth;
}
