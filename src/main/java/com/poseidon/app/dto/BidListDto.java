package com.poseidon.app.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record BidListDto( 
        Integer id,
        @Column(length = 30, nullable = false)
        @NotBlank(message = "Account is mandatory")
        String account,
        @Column(length = 30, nullable = false)
        @NotBlank(message = "Type is mandatory")
        String type,
        Double bidQuantity) {}
       
