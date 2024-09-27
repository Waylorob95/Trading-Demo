package com.stan.cryptoTrading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stan.cryptoTrading.modal.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;
    String getMarketChart(String coinId, int days) throws Exception;
    Coin getCoinById(String coinId) throws Exception;
    String getCoinDetails(String coinId) throws Exception;

    String searchCoin(String keyword) throws Exception;

    String getTrendingCoins() throws Exception;
}
