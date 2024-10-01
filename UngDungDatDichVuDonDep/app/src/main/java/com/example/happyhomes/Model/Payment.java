package com.example.happyhomes.Model;

import java.sql.Date;

public class Payment {
    private Long payId;
    private Long methodId;
    private Long serScheId;
    private Long serviceId;
    private Date payDay;

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

    public Date getPayDay() {
        return payDay;
    }

    public void setPayDay(Date payDay) {
        this.payDay = payDay;
    }

    public Payment(Long payId, Long methodId, Long serScheId, Long serviceId, Date payDay) {
        this.payId = payId;
        this.methodId = methodId;
        this.serScheId = serScheId;
        this.serviceId = serviceId;
        this.payDay = payDay;
    }
}
