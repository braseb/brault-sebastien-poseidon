package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.Rating;
import com.poseidon.app.dto.RatingDto;
import com.poseidon.app.repositories.RatingRepository;

@Service
public class RatingService {

    @Autowired
    RatingRepository RatingRepository;
    
    public RatingDto save(RatingDto ratingDto) {
        
        Rating rating;
        if (ratingDto.id() != null && RatingRepository.existsById(ratingDto.id())) {
            rating = RatingRepository.findById(ratingDto.id()).orElseThrow();
            rating.setMoodysRating(ratingDto.moodysRating());
            rating.setSandPRating(ratingDto.sandPRating());
            rating.setFitchRating (ratingDto.fitchRating());
            rating.setOrderNumber(ratingDto.orderNumber());
        } else {
            rating = new Rating(
                    ratingDto.moodysRating(),
                    ratingDto.sandPRating(), 
                    ratingDto.fitchRating(),
                    ratingDto.orderNumber()
            );
        }
        
        Rating saved = RatingRepository.save(rating);
                
        
        return new RatingDto(   saved.getId(),
                                    saved.getMoodysRating(), 
                                    saved.getSandPRating(), 
                                    saved.getFitchRating(),
                                    saved.getOrderNumber());
    }
    
    public List<RatingDto> getAll(){
        return RatingRepository.findAll().stream()
                                            .map(c -> {return new RatingDto(c.getId(),
                                                                                c.getMoodysRating(),
                                                                                c.getSandPRating(),
                                                                                c.getFitchRating(),
                                                                                c.getOrderNumber());})
                                            .collect(Collectors.toList());
    }
    
    public RatingDto getRatingById(Integer id) {
        Rating rating = RatingRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Rating Id:" + id));
        
        return new RatingDto(  rating.getId(),
                                   rating.getMoodysRating(), 
                                   rating.getSandPRating(),
                                   rating.getFitchRating(),
                                   rating.getOrderNumber());
    }
    
    public void deleteRatingById(Integer id) {
        RatingRepository.deleteById(id);
    }
    
}
