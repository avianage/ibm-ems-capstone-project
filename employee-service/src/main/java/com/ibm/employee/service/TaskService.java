package com.ibm.employee.service;

import com.ibm.employee.dto.request.TaskRequest;
import com.ibm.employee.dto.response.TaskResponse;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest request);
    List<TaskResponse> getTasksByEmployeeId(String employeeId);
    TaskResponse updateTaskStatus(String taskId, String status);
}
