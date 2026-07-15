package com.ibm.attendance.controller;

import com.ibm.attendance.dto.LeaveRequestDto;
import com.ibm.attendance.dto.LeaveResponseDto;
import com.ibm.attendance.service.LeaveRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaves")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping
    public LeaveResponseDto submitLeaveRequest(@Valid @RequestBody LeaveRequestDto request) {
        return leaveRequestService.submitLeaveRequest(request);
    }

    @GetMapping("/employee/{employeeId}")
    public List<LeaveResponseDto> getLeaveRequestsByEmployee(@PathVariable String employeeId) {
        return leaveRequestService.getLeaveRequestsByEmployee(employeeId);
    }

    @GetMapping
    public List<LeaveResponseDto> getAllLeaveRequests() {
        return leaveRequestService.getAllLeaveRequests();
    }

    @PatchMapping("/{id}/status")
    public LeaveResponseDto updateLeaveStatus(
            @PathVariable String id,
            @RequestParam String status,
            @RequestParam String reviewedBy,
            @RequestParam(required = false, defaultValue = "") String comments) {
        return leaveRequestService.updateLeaveStatus(id, status, reviewedBy, comments);
    }
}
