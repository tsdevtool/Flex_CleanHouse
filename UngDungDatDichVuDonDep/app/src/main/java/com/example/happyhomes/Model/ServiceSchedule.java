package com.example.happyhomes.Model;

public class ServiceSchedule {
    private Long serScheId;
    private Long serviceId;
    private Long scheduleId;

    public Long getSerScheId() {
        return serScheId;
    }

    public void setSerScheId(Long serScheId) {
        this.serScheId = serScheId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public ServiceSchedule(Long serScheId, Long serviceId, Long scheduleId) {
        this.serScheId = serScheId;
        this.serviceId = serviceId;
        this.scheduleId = scheduleId;
    }
}
