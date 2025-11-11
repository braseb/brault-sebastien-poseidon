package com.poseidon.app.dto;

import org.hibernate.validator.constraints.Length;

public record RatingDto( 
        Integer id,
        @Length(max = 125)
        String moodysRating,
        @Length(max = 125)
        String sandPRating,
        @Length(max = 125)
        String fitchRating,
        Integer orderNumber) {}
