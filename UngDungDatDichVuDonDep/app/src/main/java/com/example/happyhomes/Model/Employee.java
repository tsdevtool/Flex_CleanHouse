package com.example.happyhomes.Model;

import java.io.Serializable;

public class Employee implements Serializable {
    private Long emId;
    private String emName;
    private String emEmail;
    private String password;

    public Long getEmId() {
        return emId;
    }

    public void setEmId(Long emId) {
        this.emId = emId;
    }

    public String getEmName() {
        return emName;
    }

    public void setEmName(String emName) {
        this.emName = emName;
    }

    public String getEmEmail() {
        return emEmail;
    }

    public void setEmEmail(String emEmail) {
        this.emEmail = emEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee(Long emId, String emName, String emEmail, String password) {
        this.emId = emId;
        this.emName = emName;
        this.emEmail = emEmail;
        this.password = password;
    }
}
