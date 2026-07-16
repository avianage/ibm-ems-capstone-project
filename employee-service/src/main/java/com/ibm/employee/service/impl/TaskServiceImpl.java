package com.ibm.employee.service.impl;

import com.ibm.employee.dto.request.TaskRequest;
import com.ibm.employee.dto.response.TaskResponse;
import com.ibm.employee.entity.Task;
import com.ibm.employee.exception.ResourceNotFoundException;
import com.ibm.employee.repository.EmployeeRepository;
import com.ibm.employee.repository.TaskRepository;
import com.ibm.employee.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public TaskResponse createTask(TaskRequest request) {
        // Verify employee exists
        if (!employeeRepository.existsById(request.getEmployeeId())) {
            throw new ResourceNotFoundException("Employee not found with ID: " + request.getEmployeeId());
        }

        Task task = Task.builder()
                .employeeId(request.getEmployeeId())
                .title(request.getTitle())
                .description(request.getDescription())
                .status("PENDING")
                .assignedDate(LocalDateTime.now())
                .dueDate(request.getDueDate())
                .build();

        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    @Override
    public List<TaskResponse> getTasksByEmployeeId(String employeeId) {
        // Verify employee exists
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("Employee not found with ID: " + employeeId);
        }

        return taskRepository.findByEmployeeId(employeeId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse updateTaskStatus(String taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + taskId));

        // Validate the new status
        String normalizedStatus = status.trim().toUpperCase();
        if (!normalizedStatus.equals("PENDING") && !normalizedStatus.equals("IN_PROGRESS") && !normalizedStatus.equals("FINISHED")) {
            throw new IllegalArgumentException("Invalid task status: " + status + ". Allowed values are PENDING, IN_PROGRESS, FINISHED");
        }

        task.setStatus(normalizedStatus);
        if (normalizedStatus.equals("FINISHED")) {
            task.setCompletedDate(LocalDateTime.now());
        } else {
            task.setCompletedDate(null); // Clear completed date if moved back to in progress/pending
        }

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }

    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .employeeId(task.getEmployeeId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .assignedDate(task.getAssignedDate())
                .dueDate(task.getDueDate())
                .completedDate(task.getCompletedDate())
                .build();
    }
}
