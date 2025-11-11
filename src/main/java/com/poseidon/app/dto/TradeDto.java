package com.poseidon.app.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TradeDto( 
        Integer id,
        @Length(max = 30, message = "Max lenght is 30")
        @NotBlank(message = "Account is mandatory")
        String account,
        @Length(max = 30, message = "Max lenght is 30")
        @NotBlank(message = "Type is mandatory")
        String type,
        @Min(value = 1, message = "Buy quantity min is 1")
        Double buyQuantity) {}
