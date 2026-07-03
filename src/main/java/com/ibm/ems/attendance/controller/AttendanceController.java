package com.ibm.ems.attendance.controller;

import com.ibm.ems.attendance.dto.AttendanceRequest;
import com.ibm.ems.attendance.dto.AttendanceResponse;
import com.ibm.ems.attendance.service.AttendanceService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Test API
    @GetMapping("/test")
    public String test() {
        return "Attendance Service is Working!";
    }

    // Create Attendance API
    @PostMapping
    public AttendanceResponse createAttendance(
            @RequestBody AttendanceRequest request) {

        return attendanceService.createAttendance(request);
    }
    
    @GetMapping
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceService.getAllAttendance();
    }
    @GetMapping("/{id}")
    public AttendanceResponse getAttendanceById(@PathVariable String id) {

        return attendanceService.getAttendanceById(id);
    }
    
    @PutMapping("/{id}")
    public AttendanceResponse updateAttendance(
            @PathVariable String id,
            @RequestBody AttendanceRequest request) {

        return attendanceService.updateAttendance(id, request);
    }
    @DeleteMapping("/{id}")
    public String deleteAttendance(@PathVariable String id) {

        attendanceService.deleteAttendance(id);

        return "Attendance deleted successfully.";
    }
    
}