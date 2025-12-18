package com.codebase.microservices.AgentService.model;

import com.codebase.microservices.AgentService.entity.Agent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {
    private Agent agent;
    private int vaccinationCenter;
    private List<Citizen> citizens;
}
