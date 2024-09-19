package com.stan.cryptoTrading.repository;

import com.stan.cryptoTrading.modal.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinRepository extends JpaRepository<Coin, String> {

}
