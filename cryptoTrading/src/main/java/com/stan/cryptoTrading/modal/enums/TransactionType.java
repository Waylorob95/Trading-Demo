package com.stan.cryptoTrading.modal.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum TransactionType {
    @JsonProperty("withdrawal") WITHDRAWAL,
    @JsonProperty("deposit") DEPOSIT,
    @JsonProperty("transfer") TRANSFER,
    @JsonProperty("buy") BUY,
    @JsonProperty("sell") SELL
}
