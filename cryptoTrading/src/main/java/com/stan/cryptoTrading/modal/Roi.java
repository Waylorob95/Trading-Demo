package com.stan.cryptoTrading.modal;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Roi {

    @JsonProperty("times")
    private double times;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("percentage")
    private double percentage;

}

