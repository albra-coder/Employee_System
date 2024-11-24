package com.example.employee_system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LeaveRequest {
    private String fullName;
    private String workId;
    private String startDate;
    private String endDate;
    private String status;
    private String requestId; // Firebase key for the record

    private String reason; // New field for leave reason


    // Required empty constructor for Firebase
    public LeaveRequest() {}

    // Parameterized constructor
    public LeaveRequest(String fullName, String workId, String startDate, String endDate, String status, String reason) {
        this.fullName = fullName;
        this.workId = workId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.reason = reason;
    }

    // Getters and Setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // Validate that the leave duration does not exceed 30 days
    public boolean isDurationValid() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            long difference = end.getTime() - start.getTime();
            long days = difference / (1000 * 60 * 60 * 24);
            return days >= 0 && days <= 30;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate that the status is one of the allowed values
    public boolean isStatusValid() {
        return status.equals("Pending") || status.equals("Approved") || status.equals("Declined");
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "fullName='" + fullName + '\'' +
                ", workId='" + workId + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", status='" + status + '\'' +
                ", requestId='" + requestId + '\'' +
                '}';
    }
}
