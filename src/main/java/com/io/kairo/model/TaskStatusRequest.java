package com.io.kairo.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskStatusRequest {

    private String status;
    private LocalDate dueDate;

}
