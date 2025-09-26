package com.poseidon.app.repositories;

import com.poseidon.app.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
