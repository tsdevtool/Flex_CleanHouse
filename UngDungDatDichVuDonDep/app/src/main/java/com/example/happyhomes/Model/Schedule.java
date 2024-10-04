package com.example.happyhomes.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Schedule {
    private String id; // Thay đổi từ Long sang String
    private String customerId;
    private Date date;
    private Date startTime;
    private String location;
    private String status;;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public Schedule() {
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

    // Hàm khởi tạo của lớp Schedule
    public Schedule(String id, String customerId, Date date, Date startTime, String location, String status) {
        this.id = id;
        this.customerId = customerId;
        this.date = date;
        this.startTime = startTime;
        this.location = location;
        this.status = status;
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
