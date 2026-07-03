package com.ibm.ems.attendance.service.impl;

import org.springframework.stereotype.Service;

import com.ibm.ems.attendance.dto.AttendanceRequest;
import com.ibm.ems.attendance.dto.AttendanceResponse;
import com.ibm.ems.attendance.entity.Attendance;
import com.ibm.ems.attendance.repository.AttendanceRepository;
import com.ibm.ems.attendance.service.AttendanceService;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    // Constructor Injection
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public AttendanceResponse createAttendance(AttendanceRequest request) {

        // Basic Validation
        if (request.getEmployeeId() == null || request.getEmployeeId().isBlank()) {
            throw new IllegalArgumentException("Employee ID cannot be empty.");
        }

        // Convert Request DTO to Entity
        Attendance attendance = new Attendance();
        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setStatus(request.getStatus());

        // Save into MongoDB
        Attendance savedAttendance = attendanceRepository.save(attendance);

        // Convert Entity to Response DTO
        AttendanceResponse response = new AttendanceResponse();
        response.setId(savedAttendance.getId());
        response.setEmployeeId(savedAttendance.getEmployeeId());
        response.setStatus(savedAttendance.getStatus());

        return response;
    }
    
    @Override
    public List<AttendanceResponse> getAllAttendance() {

        List<Attendance> attendanceList = attendanceRepository.findAll();

        List<AttendanceResponse> responseList = new ArrayList<>();

        for (Attendance attendance : attendanceList) {

            AttendanceResponse response = new AttendanceResponse();

            response.setId(attendance.getId());
            response.setEmployeeId(attendance.getEmployeeId());
            response.setStatus(attendance.getStatus());

            responseList.add(response);
        }

        return responseList;
    }
    
    @Override
    public AttendanceResponse getAttendanceById(String id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setStatus(attendance.getStatus());

        return response;
    }
    @Override
    public AttendanceResponse updateAttendance(String id, AttendanceRequest request) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendance.setEmployeeId(request.getEmployeeId());
        attendance.setStatus(request.getStatus());

        Attendance updatedAttendance = attendanceRepository.save(attendance);

        AttendanceResponse response = new AttendanceResponse();
        response.setId(updatedAttendance.getId());
        response.setEmployeeId(updatedAttendance.getEmployeeId());
        response.setStatus(updatedAttendance.getStatus());

        return response;
    }
    
    @Override
    public void deleteAttendance(String id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }
}