package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.Asset;
import com.stan.cryptoTrading.modal.Coin;
import com.stan.cryptoTrading.modal.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(User user, Coin coin, double quantity);

    Asset getAssetById(Long id) throws Exception;

    List<Asset> getUserAssets(Long userId);

    Asset updateAsset(Long id, double quantity) throws Exception;

    Asset getAssetByUserIdAndCoinId(Long userId, String coinId);

    void deleteAsset(Long id);
}
