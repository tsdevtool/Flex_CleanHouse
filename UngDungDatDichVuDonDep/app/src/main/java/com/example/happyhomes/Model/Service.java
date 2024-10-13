package com.example.happyhomes.Model;

import java.io.Serializable;

public class Service implements Serializable {
    private Long serviceId;
    private String serviceType;
    private Double serviceCost;

    // No-argument constructor required by Firebase
    public Service() {
    }

    // Constructor with arguments
    public Service(Long serviceId, String serviceType, Double serviceCost) {
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.serviceCost = serviceCost;
    }

    // Getters and setters
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(Double serviceCost) {
        this.serviceCost = serviceCost;
    }
}
