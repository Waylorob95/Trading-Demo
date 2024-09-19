package com.stan.cryptoTrading.modal.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum VerificationType {
    MOBILE,
    EMAIL
}
