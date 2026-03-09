package com.xyfer.patientService.Grpc;

import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Value("${billing.service.host}")
    private String billingHost;

    @Value("${billing.service.port}")
    private int billingPort;

    @Bean
    public BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub() {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(billingHost, billingPort)
                .usePlaintext()
                .build();
        return BillingServiceGrpc.newBlockingStub(channel);
    }
}