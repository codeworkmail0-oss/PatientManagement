package com.xyfer.patientService.Dto;

import com.xyfer.patientService.Models.Gender;

import java.time.LocalDateTime;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime updatedDate,
        AddressResponse address ,
        Gender gender
) {}
