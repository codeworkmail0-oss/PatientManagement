package com.xyfer.patientService.Mapper;

import com.xyfer.patientService.Dto.*;
import com.xyfer.patientService.Models.*;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientMapper {

    public static PatientResponse toResponse(Patient patient) {
        if (patient == null) return null;

        Address address = patient.getAddress();
        AddressResponse addressResponse = null;

        if (address != null) {
            addressResponse = new AddressResponse(
                    address.getId(),
                    address.getStreetNumber(),
                    address.getStreetName(),
                    address.getApartment(),
                    address.getCity(),
                    address.getState(),
                    address.getZipCode()
            );
        }

        return new PatientResponse(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getEmail(),
                patient.getUpdatedDate(),
                addressResponse ,
                patient.getGender()
        );
    }

    public static Patient toEntity(PatientRequest request) {
        if (request == null) return null;

        AddressRequest addressRequest = request.address();
        Address address = null;

        if (addressRequest != null) {
            address = Address.builder()
                    .streetNumber(addressRequest.streetNumber())
                    .streetName(addressRequest.streetName())
                    .apartment(addressRequest.apartment())
                    .city(addressRequest.city())
                    .state(addressRequest.state())
                    .zipCode(addressRequest.zipCode())
                    .build();
        }

        return Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(address)
                .gender(request.gender())
                .build();
    }

    public static Slice<PatientResponse> toResponseSlice(Slice<Patient> patientSlice) {
        if (patientSlice == null) return null;
        List<PatientResponse> responseList = patientSlice.stream()
                .map(PatientMapper::toResponse)
                .collect(Collectors.toList());
        return new SliceImpl<>(
                responseList,
                patientSlice.getPageable(),
                patientSlice.hasNext()
        );
    }
}
