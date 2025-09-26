package com.poseidon.app.repositories;

import com.poseidon.app.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
