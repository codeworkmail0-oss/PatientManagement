package com.xyfer.patientService.Grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import billing.BillingStatusRequest;
import billing.BillingStatusResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingServiceGrpcClient {

    private final BillingServiceGrpc.BillingServiceBlockingStub billingStub;

    @CircuitBreaker(name = "billingService", fallbackMethod = "createBillingAccountFallback")
    @Retry(name = "billingService")
    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        try {
            BillingResponse response = billingStub.createBillingAccount(request);
            log.info("Billing account created for patient {}: accountId={}, status={}",
                    patientId, response.getAccountId(), response.getStatus());
            return response;
        } catch (StatusRuntimeException e) {
            log.error("Failed to create billing account for patient {}: {}", patientId, e.getStatus());
            throw new RuntimeException("Billing service unavailable", e);
        }
    }

    public BillingResponse createBillingAccountFallback(String patientId, String name, String email, Exception e) {
        log.error("Circuit breaker triggered for createBillingAccount - patient {}: {}", patientId, e.getMessage());
        return BillingResponse.newBuilder()
                .setAccountId("PENDING")
                .setStatus("BILLING_UNAVAILABLE")
                .build();
    }

    @CircuitBreaker(name = "billingService", fallbackMethod = "isBillingActiveFallback")
    @Retry(name = "billingService")
    public boolean isBillingActive(String patientId) {
        BillingStatusRequest request = BillingStatusRequest.newBuilder()
                .setPatientId(patientId)
                .build();

        try {
            BillingStatusResponse response = billingStub.getBillingStatus(request);
            return "ACTIVE".equals(response.getStatus());
        } catch (StatusRuntimeException e) {
            log.error("Failed to get billing status for patient {}: {}", patientId, e.getStatus());
            throw new RuntimeException("Billing service unavailable", e);
        }
    }

    public boolean isBillingActiveFallback(String patientId, Exception e) {
        log.error("Circuit breaker triggered for isBillingActive - patient {}: {}", patientId, e.getMessage());
        return false;
    }
}