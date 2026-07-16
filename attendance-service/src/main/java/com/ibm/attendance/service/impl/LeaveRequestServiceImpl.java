package com.ibm.attendance.service.impl;

import com.ibm.attendance.dto.LeaveRequestDto;
import com.ibm.attendance.dto.LeaveResponseDto;
import com.ibm.attendance.entity.Attendance;
import com.ibm.attendance.entity.LeaveRequest;
import com.ibm.attendance.exception.LeaveRequestNotFoundException;
import com.ibm.attendance.repository.AttendanceRepository;
import com.ibm.attendance.repository.LeaveRequestRepository;
import com.ibm.attendance.service.LeaveRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final AttendanceRepository attendanceRepository;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository, AttendanceRepository attendanceRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public LeaveResponseDto submitLeaveRequest(LeaveRequestDto request) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployeeId(request.getEmployeeId());
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setStatus("PENDING");
        leaveRequest.setRequestedAt(LocalDateTime.now());

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);
        return mapToResponse(saved);
    }

    @Override
    public List<LeaveResponseDto> getLeaveRequestsByEmployee(String employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<LeaveResponseDto> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LeaveResponseDto updateLeaveStatus(String id, String status, String reviewedBy, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException("Leave request not found with ID: " + id));

        String normalizedStatus = status.trim().toUpperCase();
        if (!normalizedStatus.equals("PENDING") && !normalizedStatus.equals("APPROVED") && !normalizedStatus.equals("REJECTED")) {
            throw new IllegalArgumentException("Invalid status: " + status + ". Allowed values are PENDING, APPROVED, REJECTED");
        }

        leaveRequest.setStatus(normalizedStatus);
        leaveRequest.setReviewedBy(reviewedBy);
        leaveRequest.setReviewedAt(LocalDateTime.now());
        leaveRequest.setComments(comments);

        LeaveRequest saved = leaveRequestRepository.save(leaveRequest);

        // If approved, create attendance logs with status "LEAVE" for every day in range
        if (normalizedStatus.equals("APPROVED")) {
            LocalDate start = saved.getStartDate();
            LocalDate end = saved.getEndDate();
            LocalDate current = start;

            while (!current.isAfter(end)) {
                // Check if attendance record already exists for this date, if not, create it
                if (attendanceRepository.findByEmployeeIdAndDate(saved.getEmployeeId(), current).isEmpty()) {
                    Attendance attendance = new Attendance();
                    attendance.setEmployeeId(saved.getEmployeeId());
                    attendance.setDate(current);
                    attendance.setStatus("LEAVE");
                    attendance.setWorkingHours(0.0);
                    attendanceRepository.save(attendance);
                } else {
                    // Update existing attendance to LEAVE status
                    Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(saved.getEmployeeId(), current).get();
                    attendance.setStatus("LEAVE");
                    attendanceRepository.save(attendance);
                }
                current = current.plusDays(1);
            }
        }

        return mapToResponse(saved);
    }

    private LeaveResponseDto mapToResponse(LeaveRequest leaveRequest) {
        LeaveResponseDto dto = new LeaveResponseDto();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployeeId());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setRequestedAt(leaveRequest.getRequestedAt());
        dto.setReviewedBy(leaveRequest.getReviewedBy());
        dto.setReviewedAt(leaveRequest.getReviewedAt());
        dto.setComments(leaveRequest.getComments());
        return dto;
    }
}
