package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String otp;

    @OneToOne
    private User user;
    private String email;
    private String mobile;
    @Enumerated(EnumType.STRING)
    private VerificationType sentTo;
}
