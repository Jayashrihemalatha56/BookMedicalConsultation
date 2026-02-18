package com.ey.capstone.bookmyconsultation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postcode;
}