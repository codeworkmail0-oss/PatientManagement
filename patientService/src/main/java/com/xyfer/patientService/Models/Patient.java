package com.xyfer.patientService.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {

    @Id
    @UuidGenerator
    private UUID id ;

    private String firstName ;

    private String lastName ;

    @Email
    @Column(unique = true)
    private String email ;

    private LocalDateTime registeredDate;

    private LocalDateTime updatedDate ;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    private Gender gender ;
}
