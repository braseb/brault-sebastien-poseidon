package com.poseidon.app.domain;

import jakarta.persistence.Column;

//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 125)
    private String moodyRating;
    @Column(length = 125)
    private String sandPRating;
    @Column(length = 125)
    private String fitchRating;
    private short orderNumber;
    
}
