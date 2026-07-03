package com.ibm.ems.attendance.dto;

import jakarta.validation.constraints.NotBlank;

public class CheckOutRequest {

    @NotBlank(message = "Employee ID is required")
    private String employeeId;

    public CheckOutRequest() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}