package com.xyfer.patientService.Dto;

import com.xyfer.patientService.ValidationGroups.OnCreate;
import jakarta.validation.constraints.NotBlank;

public record AddressRequest(

        @NotBlank(groups = OnCreate.class, message = "Street number is required")
        String streetNumber,

        @NotBlank(groups = OnCreate.class, message = "Street name is required")
        String streetName,

        String apartment,

        @NotBlank(groups = OnCreate.class, message = "City is required")
        String city,

        @NotBlank(groups = OnCreate.class, message = "State is required")
        String state,

        @NotBlank(groups = OnCreate.class, message = "Zip code is required")
        String zipCode

) {}
