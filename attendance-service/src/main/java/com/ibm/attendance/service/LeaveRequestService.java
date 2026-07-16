package com.ibm.attendance.service;

import com.ibm.attendance.dto.LeaveRequestDto;
import com.ibm.attendance.dto.LeaveResponseDto;

import java.util.List;

public interface LeaveRequestService {
    LeaveResponseDto submitLeaveRequest(LeaveRequestDto request);
    List<LeaveResponseDto> getLeaveRequestsByEmployee(String employeeId);
    List<LeaveResponseDto> getAllLeaveRequests();
    LeaveResponseDto updateLeaveStatus(String id, String status, String reviewedBy, String comments);
}
