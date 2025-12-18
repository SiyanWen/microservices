package com.codebase.microservices.AgentService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationCenter {
    private int id;
    private String centerName;
    private String centerAddress;
}
