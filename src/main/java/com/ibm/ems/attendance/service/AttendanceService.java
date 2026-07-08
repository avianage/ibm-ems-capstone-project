package com.ibm.ems.attendance.service;

import java.util.List;

import com.ibm.ems.attendance.dto.AttendanceHistoryResponse;
import com.ibm.ems.attendance.dto.AttendanceRequest;
import com.ibm.ems.attendance.dto.AttendanceResponse;
import com.ibm.ems.attendance.dto.CheckInRequest;
import com.ibm.ems.attendance.dto.CheckOutRequest;
import com.ibm.ems.attendance.dto.MonthlyReportResponse;

public interface AttendanceService {

    AttendanceResponse createAttendance(AttendanceRequest request);

    List<AttendanceResponse> getAllAttendance();
    
    AttendanceResponse getAttendanceById(String id);
    
    AttendanceResponse updateAttendance(String id, AttendanceRequest request);
    
    AttendanceResponse checkIn(CheckInRequest request);

    AttendanceResponse checkOut(CheckOutRequest request);
    
//    List<AttendanceResponse> getAttendanceHistory(String employeeId);
    
    List<AttendanceHistoryResponse> getAttendanceHistory(String employeeId);
    
    MonthlyReportResponse getMonthlyReport(String employeeId, int month, int year);	
    
    AttendanceResponse getAttendanceByEmployeeId(String employeeId);
    
    
    void deleteAttendance(String id);
}