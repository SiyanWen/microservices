package com.codebase.microservices.AgentService.feign;

import com.codebase.microservices.AgentService.model.Citizen;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "CITIZEN-SERVICE", fallbackFactory = CitizenServiceFallbackFactory.class)
public interface CitizenServiceClient {

    @GetMapping("/citizen/test")
    ResponseEntity<String> test();

    @GetMapping("/citizen/id/{id}")
    ResponseEntity<List<Citizen>> getCitizensByVaccinationCenterId(@PathVariable("id") Integer id);

    @PostMapping("/citizen/add")
    ResponseEntity<Citizen> addCitizen(@RequestBody Citizen citizen);
}
