package com.example.happyhomes.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Schedule {
    private String id; // Changed from Long to String
    private String customerId; // Make sure there is a getter and setter for this field
    private Date date;
    private Date startTime;
    private String location;
    private String status;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public Schedule() {
    }

    // Constructor with parameters
    public Schedule(String id, String customerId, Date date, Date startTime, String location, String status) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.status = status;
    }

    // Getters and setters for all fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDateString() {
        return dateFormat.format(date);
    }

    public Long getStartTimeTimestamp() {
        return startTime.getTime();
    }

    public String getStartTimeString() {
        return timeFormat.format(startTime);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public Date getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "date=" + getDateString() +
                ", startTime=" + getStartTimeString() +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
