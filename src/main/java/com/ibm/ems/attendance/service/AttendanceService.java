package com.ibm.ems.attendance.service;

import java.util.List;

import com.ibm.ems.attendance.dto.AttendanceRequest;
import com.ibm.ems.attendance.dto.AttendanceResponse;

public interface AttendanceService {

    AttendanceResponse createAttendance(AttendanceRequest request);

    List<AttendanceResponse> getAllAttendance();
    
    AttendanceResponse getAttendanceById(String id);
    
    AttendanceResponse updateAttendance(String id, AttendanceRequest request);
    
    void deleteAttendance(String id);
}