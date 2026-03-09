package com.xyfer.analytics_service.Kafka;

import com.google.protobuf.InvalidProtocolBufferException;
import com.xyfer.analytics_service.Grpc.BillingGrpcClient;
import com.xyfer.analytics_service.MailService.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaConsumer {

    private final EmailService emailService;
    private final BillingGrpcClient billingGrpcClient;

    public KafkaConsumer(EmailService emailService, BillingGrpcClient billingGrpcClient) {
        this.emailService = emailService;
        this.billingGrpcClient = billingGrpcClient;
    }

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);

            String patientId = patientEvent.getPatientId();
            String patientEmail = patientEvent.getEmail();
            String fullName = patientEvent.getFirstName() + " " + patientEvent.getLastName();

            boolean isBillingActive = billingGrpcClient.isBillingActive(patientId);

            if (isBillingActive) {
                log.info("Billing active for patient {} - sending welcome email", patientId);
                emailService.sendSimpleEmail(
                        patientEmail,
                        "Welcome to Our Healthcare System",
                        "Dear " + fullName + ", your account and billing have been successfully set up!"
                );
            } else {
                log.warn("Billing NOT active for patient {} - sending failure email", patientId);
                emailService.sendSimpleEmail(
                        patientEmail,
                        "Action Required: Billing Setup Failed",
                        "Dear " + fullName + ", your patient account was created successfully " +
                                "but we encountered an issue setting up your billing account. " +
                                "Please contact support or try again later."
                );
            }

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing PatientEvent: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error processing PatientEvent: {}", e.getMessage());
        }
    }
}