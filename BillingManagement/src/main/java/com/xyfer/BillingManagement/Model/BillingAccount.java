package com.xyfer.BillingManagement.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class BillingAccount {
    @Id
    @UuidGenerator
    private UUID accountId;
    private String patientId;
    private String name;
    private String email;
    private String status;
}
