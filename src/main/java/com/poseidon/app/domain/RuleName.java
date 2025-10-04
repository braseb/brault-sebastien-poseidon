package com.poseidon.app.domain;

import jakarta.persistence.Column;

//import javax.validation.constraints.NotBlank;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rulename")
public class RuleName {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 125)
    private String name;
    @Column(length = 125)
    private String description;
    @Column(length = 125)
    private String json;
    @Column(length = 512)
    private String template;
    @Column(length = 125)
    private String sqlStr;
    @Column(length = 125)
    private String sqlPart;
}
