package com.ibm.employee.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

    private String id;
    private String employeeId;
    private String title;
    private String description;
    private String status;
    private LocalDateTime assignedDate;
    private LocalDateTime dueDate;
    private LocalDateTime completedDate;
}
