package com.poseidon.app.dto;

import jakarta.validation.constraints.NotNull;

public record CurvePointDto( 
    Integer id,
    @NotNull(message = "Must not be null")
    Integer curveId,
    Double term,
    Double value) {}
