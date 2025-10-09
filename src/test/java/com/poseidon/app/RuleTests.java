package com.poseidon.app;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.repositories.RuleNameRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	@Test
	public void ruleTest() {
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");

		// Save
		rule = ruleNameRepository.save(rule);
		assertNotNull(rule.getId());
		assertTrue(rule.getName().equals("Rule Name"));
		
		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameRepository.save(rule);
		assertTrue(rule.getName().equals("Rule Name Update"));
		

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		assertTrue(listResult.size()>0);
		
		// Delete
		Integer id = rule.getId();
		ruleNameRepository.delete(rule);
		Optional<RuleName> ruleControl = ruleNameRepository.findById(id);
		assertFalse(ruleControl.isPresent());
		
	}
}
