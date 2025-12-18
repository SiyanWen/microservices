package com.codebase.microservices.AgentService.repository;

import com.codebase.microservices.AgentService.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Integer> {
    List<Agent> findByVaccinationCenterId(Integer vaccinationCenterId);
}
