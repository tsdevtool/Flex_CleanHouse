package com.example.happyhomes.Model;

public class Payment {
    private Long payId;
    private Long methodId;
    private Long serScheId;
    private Long serviceId;
    private String payDay;  // Thay đổi từ java.sql.Date sang String

    public Long getPayId() {
        return payId;
    }

    public void setPayId(Long payId) {
        this.payId = payId;
    }

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

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

    public String getPayDay() {
        return payDay;
    }

    public void setPayDay(String payDay) {
        this.payDay = payDay;
    }

    public Payment(Long payId, Long methodId, Long serScheId, Long serviceId, String payDay) {
        this.payId = payId;
        this.methodId = methodId;
        this.serScheId = serScheId;
        this.serviceId = serviceId;
        this.payDay = payDay;
    }
}
