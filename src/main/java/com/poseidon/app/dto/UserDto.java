package com.poseidon.app.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserDto( 
        Integer id,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Username is mandatory")
        @Column(unique = true)
        String username,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Password is mandatory")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$",
                message = "The password must contain at least 8 characters, one uppercase letter, one number, and one symbol."
            )
        String password,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "FullName is mandatory")
        String fullname,
        @Length(max = 125, message = "Max lenght is 125")
        @NotBlank(message = "Role is mandatory")
        String role) {}
