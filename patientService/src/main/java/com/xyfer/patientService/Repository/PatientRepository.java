package com.xyfer.patientService.Repository;

import com.xyfer.patientService.Models.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.xyfer.patientService.ValidationGroups.*;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient , UUID> {
    Slice<Patient> findByUpdatedDateBeforeOrderByUpdatedDateDesc(
            java.time.LocalDateTime cursor,
            Pageable pageable
    );

    @Query("SELECT p.email FROM Patient p WHERE p.email IN :emails")
    List<String> findEmailsByEmailIn(List<String> emails);

    boolean existsByEmail(@Email(groups = {OnCreate.class, OnUpdate.class}, message = "Email should be valid") @NotBlank(groups = OnCreate.class, message = "Email is required") String email);
}
