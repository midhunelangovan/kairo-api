package com.io.kairo.controller;

import com.io.kairo.model.Task;
import com.io.kairo.model.TaskStatusRequest;
import com.io.kairo.service.TaskService;
import com.io.kairo.service.TaskSummaryService;
import jakarta.validation.Valid;
import kals.com.core.model.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskSummaryService taskSummaryService;

    public TaskController(TaskService taskService, TaskSummaryService taskSummaryService) {
        this.taskService = taskService;
        this.taskSummaryService = taskSummaryService;
    }

    @GetMapping("")
    public ResponseEntity<PageResponse<Task>> getAllTaskList(
            @RequestParam(name = "q", required = false) String query,
            Pageable pageable
    ) {
        PageResponse<Task> taskList = taskService.getAllTask(query, pageable);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id
    ) {
        Task task = taskService.getTaskById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Task> createTask(
            @Valid @RequestBody Task task
    ) {
        Task taskRes = taskService.createTask(task);
        return new ResponseEntity<>(taskRes, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(
            @Valid @RequestBody Task task,
            @PathVariable Long id
    ) {
        Task taskRes = taskService.updateTask(task, id);
        return new ResponseEntity<>(taskRes, HttpStatus.OK);
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @RequestBody TaskStatusRequest status,
            @PathVariable Long id
    ) {
        Task taskRes = taskService.updateTaskStatus(status, id);
        return new ResponseEntity<>(taskRes, HttpStatus.OK);
    }

    @PatchMapping("/{id}/due-date")
    public ResponseEntity<Task> updateTaskDueDate(
            @RequestBody TaskStatusRequest status,
            @PathVariable Long id
    ) {
        Task taskRes = taskService.updateTaskDueDate(status, id);
        return new ResponseEntity<>(taskRes, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/archive")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/today")
    public ResponseEntity<PageResponse<Task>> getTodayTaskList(
            Pageable pageable
    ) {
        PageResponse<Task> pageResponse = taskSummaryService.getAllTodayTask(pageable);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/overdue")
    public ResponseEntity<PageResponse<Task>> getOverdueTaskList(
            Pageable pageable
    ) {
        PageResponse<Task> pageResponse = taskSummaryService.getAllOverdueTaskList(pageable);
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
