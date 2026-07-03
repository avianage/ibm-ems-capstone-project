package com.ibm.ems.attendance.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ibm.ems.attendance.entity.Attendance;

public interface AttendanceRepository
extends MongoRepository<Attendance, String> {

}