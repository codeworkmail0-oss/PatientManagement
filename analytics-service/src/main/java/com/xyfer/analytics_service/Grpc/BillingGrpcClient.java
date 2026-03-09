package com.xyfer.analytics_service.Grpc;

import billing.BillingServiceGrpc;
import billing.BillingStatusRequest;
import billing.BillingStatusResponse;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BillingGrpcClient {

    @GrpcClient("billingService")
    private BillingServiceGrpc.BillingServiceBlockingStub billingStub;

    public boolean isBillingActive(String patientId) {
        try {
            BillingStatusRequest request = BillingStatusRequest.newBuilder()
                    .setPatientId(patientId)
                    .build();
            BillingStatusResponse response = billingStub.getBillingStatus(request);
            return "ACTIVE".equals(response.getStatus());
        } catch (StatusRuntimeException e) {
            log.error("Failed to check billing status for patient {}: {}", patientId, e.getStatus());
            return false;
        }
    }
}