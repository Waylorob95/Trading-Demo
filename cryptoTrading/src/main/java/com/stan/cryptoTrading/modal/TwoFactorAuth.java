package com.stan.cryptoTrading.modal;

import com.stan.cryptoTrading.modal.enums.VerificationType;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class TwoFactorAuth {
    private boolean isEnable = false;
    private VerificationType sentTo;

}
