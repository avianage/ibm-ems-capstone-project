package com.ibm.ems.attendance.mapper;

import com.ibm.ems.attendance.dto.AttendanceResponse;
import com.ibm.ems.attendance.entity.Attendance;

public class AttendanceMapper {

    public static AttendanceResponse toResponse(Attendance attendance) {

        AttendanceResponse response = new AttendanceResponse();

        response.setId(attendance.getId());
        response.setEmployeeId(attendance.getEmployeeId());
        response.setStatus(attendance.getStatus());

        return response;
    }

}