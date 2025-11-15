package com.poseidon.app.services;

import com.poseidon.app.domain.Rating;
import com.poseidon.app.dto.RatingDto;
import com.poseidon.app.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    
    @InjectMocks
    private RatingService ratingService;

    private Rating rating;
    

    @BeforeEach
    void setUp() {
        rating = new Rating("moodysRating", "sandPRating", "fitchRating", 3);
        
    }

    
    @Test
    void save_ShouldCreateNewRating() {
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        RatingDto result = ratingService.save(new RatingDto(1,
                                                           rating.getMoodysRating(),
                                                           rating.getSandPRating(),
                                                           rating.getFitchRating(),
                                                           rating.getOrderNumber()));

        assertEquals("moodysRating", result.moodysRating());
        assertEquals("sandPRating", result.sandPRating());
        assertEquals(3, result.orderNumber());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void save_ShouldUpdateExistingRating_WhenIdExists(){
        when(ratingRepository.existsById(1)).thenReturn(true);
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        RatingDto updatedDto = new RatingDto(1, "moodysRatingNew","sandPRatingNew","fitchRatingNew", 4);
        RatingDto result;
        result = ratingService.save(updatedDto);
       
        assertEquals("moodysRatingNew", result.moodysRating());
        assertEquals("sandPRatingNew", result.sandPRating());
        assertEquals("fitchRatingNew", result.fitchRating());
        assertEquals(4, result.orderNumber());
        verify(ratingRepository).save(any(Rating.class));
    }

    
    @Test
    void getAll_ShouldReturnListOfRatings() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        List<RatingDto> result = ratingService.getAll();

        assertEquals(1, result.size());
        assertEquals("moodysRating", result.get(0).moodysRating());
        verify(ratingRepository).findAll();
    }

   
    @Test
    void getRatingById_ShouldReturnRating_WhenFound() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));

        RatingDto result = ratingService.getRatingById(1);

        assertEquals("moodysRating", result.moodysRating());
        verify(ratingRepository).findById(1);
    }

    @Test
    void getRatingById_ShouldThrowException_WhenNotFound() {
        when(ratingRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ratingService.getRatingById(1));
    }

    
    @Test
    void deleteRatingById_ShouldCallRepository() {
        ratingService.deleteRatingById(1);
        verify(ratingRepository).deleteById(1);
    }
    
}

