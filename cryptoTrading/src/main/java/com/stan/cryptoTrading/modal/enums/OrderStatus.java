package com.stan.cryptoTrading.modal.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum OrderStatus {
    PENDING,
    FAILED,
    COMPLETED
}
