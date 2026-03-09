package com.xyfer.patientService.Controller;

import com.xyfer.patientService.Dto.*;
import com.xyfer.patientService.Service.PatientService;
import com.xyfer.patientService.ValidationGroups.OnCreate;
import com.xyfer.patientService.ValidationGroups.OnUpdate;
import org.springframework.data.domain.Slice;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public PatientResponse createPatient(
            @Validated(OnCreate.class) @RequestBody PatientRequest request) {
        return patientService.createPatient(request);
    }

    @GetMapping("/{id}")
    public PatientResponse getPatientById(@PathVariable UUID id) {
        return patientService.getPatientById(id);
    }

    @PutMapping("/{id}")
    public PatientResponse updatePatient(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody PatientRequest request) {
        return patientService.updatePatient(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
    }

    @GetMapping
    public Slice<PatientResponse> getPatients(
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        LocalDateTime cursorTime = cursor != null ? LocalDateTime.parse(cursor) : LocalDateTime.now();
        return patientService.getPatientsAfter(cursorTime, pageSize);
    }

    @PostMapping("/bulk")
    public List<PatientResponse> createPatientsBulk(
            @Validated(OnCreate.class) @RequestBody List<PatientRequest> requests) {
        return patientService.createPatientsBulk(requests);
    }


    @GetMapping("/{id}/check")
    public Boolean checkTheBillingAccount(@PathVariable UUID id){
        return patientService.checkTheBillingAccount(id) ;
    }
}