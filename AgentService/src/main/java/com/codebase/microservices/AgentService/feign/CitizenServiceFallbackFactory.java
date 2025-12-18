package com.codebase.microservices.AgentService.feign;

import com.codebase.microservices.AgentService.model.Citizen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CitizenServiceFallbackFactory implements FallbackFactory<CitizenServiceClient> {

    private static final Logger logger = LoggerFactory.getLogger(CitizenServiceFallbackFactory.class);

    @Override
    public CitizenServiceClient create(Throwable cause) {
        logger.error("CitizenService fallback triggered due to: {}", cause.getMessage());

        return new CitizenServiceClient() {
            @Override
            public ResponseEntity<String> test() {
                return new ResponseEntity<>("CitizenService is unavailable: " + cause.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
            }

            @Override
            public ResponseEntity<List<Citizen>> getCitizensByVaccinationCenterId(Integer id) {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
            }

            @Override
            public ResponseEntity<Citizen> addCitizen(Citizen citizen) {
                return new ResponseEntity<>(null, HttpStatus.SERVICE_UNAVAILABLE);
            }
        };
    }
}
