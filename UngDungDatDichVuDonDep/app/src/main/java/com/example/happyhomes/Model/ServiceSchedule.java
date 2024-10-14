package com.example.happyhomes.Model;

public class ServiceSchedule {
    private String serScheId; // Change to String
    private Long serviceId;
    private String scheduleId; // Change to String

    // Getter and Setter for serScheId
    public String getSerScheId() {
        return serScheId;
    }

    public void setSerScheId(String serScheId) {
        this.serScheId = serScheId;
    }

    // Getter and Setter for serviceId
    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    // Getter and Setter for scheduleId
    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    // Constructor
    public ServiceSchedule(String serScheId, Long serviceId, String scheduleId) {
        this.serScheId = serScheId;
        this.serviceId = serviceId;
        this.scheduleId = scheduleId;
    }
}
