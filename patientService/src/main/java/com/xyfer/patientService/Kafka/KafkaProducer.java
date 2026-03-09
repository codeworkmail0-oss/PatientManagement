package com.xyfer.patientService.Kafka;

import com.xyfer.patientService.Models.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaProducer {

    private final KafkaTemplate<String , byte[]> kafkaTemplate;

    public void sendEvent(Patient patient){

        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setFirstName(patient.getFirstName())
                .setLastName(patient.getLastName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();
        try{
            kafkaTemplate.send("patient" , event.toByteArray()) ;
        } catch (Exception e) {
            log.error("muh me lele aab error aagaya : {}" , event) ;
        }
    }
}