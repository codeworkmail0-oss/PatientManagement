package com.xyfer.patientService.Service;

import com.xyfer.patientService.Dto.*;
import com.xyfer.patientService.Exception.DuplicatePatientException;
import com.xyfer.patientService.Exception.PatientNotFoundException;
import com.xyfer.patientService.Grpc.BillingServiceGrpcClient;
import com.xyfer.patientService.Kafka.KafkaProducer;
import com.xyfer.patientService.Mapper.PatientMapper;
import com.xyfer.patientService.Models.Patient;
import com.xyfer.patientService.Repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingClient;
    private final KafkaProducer kafkaProducer;

    public PatientResponse createPatient(PatientRequest request) {

        if (patientRepository.existsByEmail(request.email())) {
            throw new DuplicatePatientException("Email already registered: " + request.email());
        }

        Patient patient = PatientMapper.toEntity(request);
        patient.setRegisteredDate(LocalDateTime.now());
        patient.setUpdatedDate(LocalDateTime.now());
        Patient saved = patientRepository.save(patient);


        billingClient.createBillingAccount(
                saved.getId().toString(),
                saved.getFirstName() + " " + saved.getLastName(),
                saved.getEmail()
        );


        kafkaProducer.sendEvent(saved);

        return PatientMapper.toResponse(saved);
    }

    public PatientResponse getPatientById(UUID id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        return PatientMapper.toResponse(patient);
    }

    public PatientResponse updatePatient(UUID id, PatientRequest request) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        existing.setFirstName(request.firstName());
        existing.setLastName(request.lastName());
        existing.setEmail(request.email());

        if (request.address() != null) {
            var addrReq = request.address();
            var address = existing.getAddress();
            if (address == null) {
                address = PatientMapper.toEntity(PatientRequest.builder()
                        .firstName(request.firstName())
                        .lastName(request.lastName())
                        .email(request.email())
                        .address(addrReq)
                        .build()
                ).getAddress();
                existing.setAddress(address);
            } else {
                address.setStreetNumber(addrReq.streetNumber());
                address.setStreetName(addrReq.streetName());
                address.setApartment(addrReq.apartment());
                address.setCity(addrReq.city());
                address.setState(addrReq.state());
                address.setZipCode(addrReq.zipCode());
            }
        }

        existing.setUpdatedDate(LocalDateTime.now());
        Patient updated = patientRepository.save(existing);
        return PatientMapper.toResponse(updated);
    }

    public void deletePatient(UUID id) {
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        patientRepository.delete(existing);
    }

    public Slice<PatientResponse> getPatientsAfter(LocalDateTime cursor, int pageSize) {
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Slice<Patient> patients = patientRepository.findByUpdatedDateBeforeOrderByUpdatedDateDesc(cursor, pageRequest);
        return PatientMapper.toResponseSlice(patients);
    }

    public List<PatientResponse> createPatientsBulk(List<PatientRequest> requests) {
        Set<String> seenEmails = new HashSet<>();
        List<String> duplicatesInBatch = requests.stream()
                .map(PatientRequest::email)
                .filter(email -> !seenEmails.add(email))
                .toList();

        if (!duplicatesInBatch.isEmpty()) {
            throw new DuplicatePatientException("Duplicate emails within request: " + duplicatesInBatch);
        }

        List<String> emails = requests.stream().map(PatientRequest::email).toList();
        List<String> existingEmails = patientRepository.findEmailsByEmailIn(emails);

        if (!existingEmails.isEmpty()) {
            throw new DuplicatePatientException("Emails already registered: " + existingEmails);
        }

        return requests.stream()
                .map(this::createPatient)
                .toList();
    }

    public Boolean checkTheBillingAccount(UUID id) {
        return billingClient.isBillingActive(id.toString());
    }
}