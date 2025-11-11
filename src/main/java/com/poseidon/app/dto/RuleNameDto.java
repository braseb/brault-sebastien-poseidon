package com.poseidon.app.dto;

import org.hibernate.validator.constraints.Length;

public record RuleNameDto( 
        Integer id,
        @Length(max = 125, message = "Max lenght is 125")
        String name,
        @Length(max = 125, message = "Max lenght is 125")
        String description,
        @Length(max = 125, message = "Max lenght is 125")
        String json,
        @Length(max = 512, message = "Max lenght is 512")
        String template,
        @Length(max = 125, message = "Max lenght is 125")
        String sqlStr,
        @Length(max = 125, message = "Max lenght is 125")
        String sqlPart) {}
