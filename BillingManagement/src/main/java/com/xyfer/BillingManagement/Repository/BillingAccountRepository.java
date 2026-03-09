package com.xyfer.BillingManagement.Repository;

import com.xyfer.BillingManagement.Model.BillingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillingAccountRepository extends JpaRepository<BillingAccount, String> {
    Optional<BillingAccount> findByPatientId(String patientId);
}
