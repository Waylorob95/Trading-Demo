package com.stan.cryptoTrading.repository;

import com.stan.cryptoTrading.modal.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByUserId(Long userId);
    Asset findByUserIdAndCoinId(Long userId, String coinId);
}
