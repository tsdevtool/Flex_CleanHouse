package com.example.happyhomes.Model;

public class Workdate {
    private String workDateId;
    private String emId;
    private String  serScheId;
    private String status;

    public Workdate() {
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkDateId() {
        return workDateId;
    }

    public void setWorkDateId(String workDateId) {
        this.workDateId = workDateId;
    }

    public String getEmId() {
        return emId;
    }

    public void setEmId(String emId) {
        this.emId = emId;
    }

    public String getSerScheId() {
        return serScheId;
    }

    public void setSerScheId(String serScheId) {
        this.serScheId = serScheId;
    }

    public Workdate(String workDateId, String emId, String serScheId, String status) {
        this.workDateId = workDateId;
        this.emId = emId;
        this.serScheId = serScheId;
        this.status = status;
    }
}
