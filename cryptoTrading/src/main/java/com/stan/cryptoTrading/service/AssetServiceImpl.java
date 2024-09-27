package com.stan.cryptoTrading.service;

import com.stan.cryptoTrading.modal.Asset;
import com.stan.cryptoTrading.modal.Coin;
import com.stan.cryptoTrading.modal.User;
import com.stan.cryptoTrading.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService{

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset asset = new Asset();
        asset.setUser(user);
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setPrice(coin.getCurrentPrice());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long id) throws Exception {

        return assetRepository.findById(id).orElseThrow(() -> new Exception("No asset found"));
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {

        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long id, double quantity) throws Exception {
        Asset asset = getAssetById(id);
        asset.setQuantity(asset.getQuantity() + quantity);

        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetByUserIdAndCoinId(Long userId, String coinId) {

        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long id) {

        assetRepository.deleteById(id);
    }
}
