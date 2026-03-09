package com.xyfer.BillingManagement.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import billing.BillingStatusRequest;
import billing.BillingStatusResponse;
import com.xyfer.BillingManagement.Model.BillingAccount;
import com.xyfer.BillingManagement.Repository.BillingAccountRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class BillingGrpcService extends BillingServiceImplBase {

    private final BillingAccountRepository billingAccountRepository;

    @Override
    public void createBillingAccount(BillingRequest request,
                                     StreamObserver<BillingResponse> responseObserver) {

        log.info("Received createBillingAccount request for patientId: {}", request.getPatientId());
        if (billingAccountRepository.findByPatientId(request.getPatientId()).isPresent()) {
            log.warn("Billing account already exists for patientId: {}", request.getPatientId());
            responseObserver.onError(
                    io.grpc.Status.ALREADY_EXISTS
                            .withDescription("Billing account already exists for patient: " + request.getPatientId())
                            .asRuntimeException()
            );
            return;
        }

        BillingAccount account = new BillingAccount();
        account.setPatientId(request.getPatientId());
        account.setName(request.getName());
        account.setEmail(request.getEmail());
        account.setStatus("ACTIVE");

        BillingAccount saved = billingAccountRepository.save(account);
        log.info("Billing account created with accountId: {} for patientId: {}",
                saved.getAccountId(), saved.getPatientId());
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(saved.getAccountId().toString())
                .setStatus(saved.getStatus())
                .build();

        responseObserver.onNext(response); 
        responseObserver.onCompleted();
    }

    @Override
    public void getBillingStatus(BillingStatusRequest request,
                                 StreamObserver<BillingStatusResponse> responseObserver) {

        log.info("Received getBillingStatus request for patientId: {}", request.getPatientId());

        String status = billingAccountRepository
                .findByPatientId(request.getPatientId())
                .map(BillingAccount::getStatus)
                .orElse("NOT_FOUND");

        log.info("Billing status for patientId {}: {}", request.getPatientId(), status);
        BillingStatusResponse response = BillingStatusResponse.newBuilder()
                .setStatus(status)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}