package com.poseidon.app.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public record UserDto( 
        Integer id,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Username is mandatory")
        @Column(unique = true)
        String username,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Password is mandatory")
        String password,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "FullName is mandatory")
        String fullname,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Role is mandatory")
        String role) {}
