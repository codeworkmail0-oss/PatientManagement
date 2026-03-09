package com.xyfer.patientService.Models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Address {
    @Id
    @UuidGenerator
    private UUID id ;
    private String streetNumber;
    private String streetName;
    private String apartment;
    private String city;
    private String state;
    private String zipCode;
}
