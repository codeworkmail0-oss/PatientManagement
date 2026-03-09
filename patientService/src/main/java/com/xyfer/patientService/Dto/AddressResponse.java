package com.xyfer.patientService.Dto;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        String streetNumber,
        String streetName,
        String apartment,
        String city,
        String state,
        String zipCode
) {}
