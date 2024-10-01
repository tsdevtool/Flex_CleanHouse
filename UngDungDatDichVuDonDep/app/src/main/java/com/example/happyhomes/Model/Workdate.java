package com.example.happyhomes.Model;

public class Workdate {
    private Long workDateId;
    private Long emId;
    private Long serScheId;

    public Long getWorkDateId() {
        return workDateId;
    }

    public void setWorkDateId(Long workDateId) {
        this.workDateId = workDateId;
    }

    public Long getEmId() {
        return emId;
    }

    public void setEmId(Long emId) {
        this.emId = emId;
    }

    public Long getSerScheId() {
        return serScheId;
    }

    public void setSerScheId(Long serScheId) {
        this.serScheId = serScheId;
    }

    public Workdate(Long workDateId, Long emId, Long serScheId) {
        this.workDateId = workDateId;
        this.emId = emId;
        this.serScheId = serScheId;
    }
}
