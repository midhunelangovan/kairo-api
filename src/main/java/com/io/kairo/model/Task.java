package com.io.kairo.model;

import com.io.kairo.enums.TaskPriority;
import com.io.kairo.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Validated
public class Task {

    private Long id;
    @NotBlank(message = "Title cannot be empty")
    private String title;
    private String description;
    private TaskStatus status = TaskStatus.TODO;
    private TaskPriority priority = TaskPriority.LOW;
    private LocalDate dueDate;
    private LocalDateTime createdAt;


}
