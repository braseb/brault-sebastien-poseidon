package com.poseidon.app;

import com.poseidon.app.domain.Rating;
import com.poseidon.app.repositories.RatingRepository;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;


@SpringBootTest
public class RatingTests {

	@Autowired
	private RatingRepository ratingRepository;

	@Test
	public void ratingTest() {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);

		// Save
		rating = ratingRepository.save(rating);
		assertNotNull(rating.getId());
		assertTrue(rating.getOrderNumber() == 10);
		
		// Update
		rating.setOrderNumber(20);
		rating = ratingRepository.save(rating);
		assertTrue(rating.getOrderNumber() == 20);

		// Find
		List<Rating> listResult = ratingRepository.findAll();
		assertTrue(listResult.size() > 0);
		
		// Delete
		Integer id = rating.getId();
		ratingRepository.delete(rating);
		Optional<Rating> ratingControl = ratingRepository.findById(id);
		assertFalse(ratingControl.isPresent());
		
	}
}
