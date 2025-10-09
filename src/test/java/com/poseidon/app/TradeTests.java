package com.poseidon.app;

import com.poseidon.app.domain.Trade;
import com.poseidon.app.repositories.TradeRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;


@SpringBootTest
public class TradeTests {

	@Autowired
	private TradeRepository tradeRepository;

	@Test
	public void tradeTest() {
		Trade trade = new Trade("Trade Account", "Type");

		// Save
		trade = tradeRepository.save(trade);
		assertNotNull(trade.getId());
		assertTrue(trade.getAccount().equals("Trade Account"));
		
		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
		assertTrue(trade.getAccount().equals("Trade Account Update"));
		
		// Find
		List<Trade> listResult = tradeRepository.findAll();
		assertTrue(listResult.size() > 0);
		
		// Delete
		Integer id = trade.getId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeControl = tradeRepository.findById(id);
		assertFalse(tradeControl.isPresent());
	}
}
