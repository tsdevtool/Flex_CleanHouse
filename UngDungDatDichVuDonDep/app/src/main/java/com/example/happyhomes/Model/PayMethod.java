package com.example.happyhomes.Model;

public class PayMethod {
    private Long methodId;
    private String method;

    public Long getMethodId() {
        return methodId;
    }

    public void setMethodId(Long methodId) {
        this.methodId = methodId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PayMethod(Long methodId, String method) {
        this.methodId = methodId;
        this.method = method;
    }
}
