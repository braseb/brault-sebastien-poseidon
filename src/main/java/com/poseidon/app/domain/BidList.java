package com.poseidon.app.domain;


import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bidlist")
public class BidList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 30, nullable = false)
    @NotBlank(message = "Account is mandatory")
    private String account;
    @Column(length = 30, nullable = false)
    @NotBlank(message = "Type is mandatory")
    private String type;
    private double bidQuantity;
    private double askQuantity;
    private double bid;
    private double ask;
    @Column(length = 125)
    private String benchmark;
    private Timestamp   bidListDate;
    @Column(length = 125)
    private String commentary;
    @Column(length = 125)
    private String security;
    @Column(length = 10)
    private String status;
    @Column(length = 125)
    private String trader;
    @Column(length = 125)
    private String book;
    @Column(length = 125)
    private String creationName;
    private Timestamp creationDate;
    @Column(length = 125)
    private String revisionName;
    private Timestamp revisionDate;
    @Column(length = 125)
    private String dealName;
    @Column(length = 125)
    private String dealType;
    @Column(length = 125)
    private String sourceListId;
    @Column(length = 125)
    private String side;
    
    public BidList(String account, String type, double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}
