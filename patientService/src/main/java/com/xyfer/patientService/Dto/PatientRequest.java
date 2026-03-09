package com.xyfer.patientService.Dto;

import com.xyfer.patientService.Models.Gender;
import com.xyfer.patientService.ValidationGroups.OnCreate;
import com.xyfer.patientService.ValidationGroups.OnUpdate;
import lombok.Builder;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
public record PatientRequest(

        @NotBlank(groups = OnCreate.class, message = "First name is required")
        String firstName,

        @NotBlank(groups = OnCreate.class, message = "Last name is required")
        String lastName,

        @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Email should be valid")
        @NotBlank(groups = OnCreate.class, message = "Email is required")
        String email,

        @NotNull(groups = OnCreate.class, message = "Address is required")
        AddressRequest address,

        @NotNull(groups = OnCreate.class, message = "Gender is required")
        Gender gender

) {}
