package com.codebase.microservices.AgentService.controller;

import com.codebase.microservices.AgentService.entity.Agent;
import com.codebase.microservices.AgentService.feign.CitizenServiceClient;
import com.codebase.microservices.AgentService.feign.VaccinationCenterClient;
import com.codebase.microservices.AgentService.model.AgentResponse;
import com.codebase.microservices.AgentService.model.Citizen;
import com.codebase.microservices.AgentService.model.VaccinationCenter;
import com.codebase.microservices.AgentService.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private CitizenServiceClient citizenServiceClient;

    @Autowired
    private VaccinationCenterClient vaccinationCenterClient;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("Hello! AgentService is live", HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Agent> addAgent(@RequestBody Agent agent) {
        Agent savedAgent = agentRepository.save(agent);
        return new ResponseEntity<>(savedAgent, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Agent> getAgentById(@PathVariable Integer id) {
        Agent agent = agentRepository.findById(id).orElse(null);
        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    @GetMapping("/bycenter/{centerId}")
    public ResponseEntity<List<Agent>> getAgentsByVaccinationCenterId(@PathVariable Integer centerId) {
        List<Agent> agents = agentRepository.findByVaccinationCenterId(centerId);
        return new ResponseEntity<>(agents, HttpStatus.OK);
    }

    // Endpoint using OpenFeign to call CitizenService
    @GetMapping("/citizens/{centerId}")
    public ResponseEntity<List<Citizen>> getCitizensFromCitizenService(@PathVariable Integer centerId) {
        ResponseEntity<List<Citizen>> response = citizenServiceClient.getCitizensByVaccinationCenterId(centerId);
        return response;
    }

    // Endpoint using OpenFeign to call VaccinationCenter
    @GetMapping("/center/{centerId}")
    public ResponseEntity<Object> getVaccinationCenter(@PathVariable Integer centerId) {
        ResponseEntity<Object> response = vaccinationCenterClient.getCenterById(centerId);
        return response;
    }

    // Endpoint using OpenFeign to test CitizenService
    @GetMapping("/citizen-service/test")
    public ResponseEntity<String> testCitizenService() {
        return citizenServiceClient.test();
    }

    // Composite endpoint - Get agent with vaccination center and citizens info
    @GetMapping("/details/{agentId}")
    public ResponseEntity<AgentResponse> getAgentWithDetails(@PathVariable Integer agentId) {
        Agent agent = agentRepository.findById(agentId).orElse(null);
        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        AgentResponse response = new AgentResponse();
        response.setAgent(agent);

        // Get Citizen from Citizen Service via OpenFeign
        try {
            int vaccinationCenter = agent.getVaccinationCenterId();
            response.setVaccinationCenter(vaccinationCenter);
            ResponseEntity<List<Citizen>> citizenResponse = citizenServiceClient.getCitizensByVaccinationCenterId(vaccinationCenter);
            response.setCitizens(citizenResponse.getBody());
        } catch (Exception e) {
            response.setVaccinationCenter(0);
            response.setCitizens(null);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Add a new citizen via OpenFeign
    @PostMapping("/citizen/add")
    public ResponseEntity<Citizen> addCitizenViaFeign(@RequestBody Citizen citizen) {
        return citizenServiceClient.addCitizen(citizen);
    }

    // Add a new vaccination center via OpenFeign
    @PostMapping("/center/add")
    public ResponseEntity<VaccinationCenter> addCenterViaFeign(@RequestBody VaccinationCenter center) {
        return vaccinationCenterClient.addCenter(center);
    }
}
