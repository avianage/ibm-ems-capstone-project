package com.ibm.ems.attendance.dto;

public class AttendanceRequest {

    private String employeeId;
    private String status;

    public AttendanceRequest() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}