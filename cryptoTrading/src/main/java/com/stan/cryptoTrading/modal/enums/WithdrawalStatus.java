package com.stan.cryptoTrading.modal.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum WithdrawalStatus {
    @JsonProperty("pending") PENDING,
    @JsonProperty("completed") COMPLETED,
    @JsonProperty("failed") FAILED
}
