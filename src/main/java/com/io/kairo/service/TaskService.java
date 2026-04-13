package com.io.kairo.service;

import com.io.kairo.entity.TaskEntity;
import com.io.kairo.enums.TaskPriority;
import com.io.kairo.enums.TaskStatus;
import com.io.kairo.mapper.TaskMapper;
import com.io.kairo.model.Task;
import com.io.kairo.model.TaskStatusRequest;
import com.io.kairo.repository.TaskRepository;
import kals.com.core.exception.DataValidationException;
import kals.com.core.exception.ResourceNotFoundException;
import kals.com.core.model.PageResponse;
import kals.com.core.specification.CommonSpecification;
import kals.com.core.utility.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a paginated list of tasks.
     * Converts the database Page of entities into a standardized PageResponse DTO.
     */
    public PageResponse<Task> getAllTask(String query, Pageable pageable) {
        CommonSpecification<TaskEntity> specification = new CommonSpecification<>(query);
        Page<TaskEntity> taskEntityPage = taskRepository.findAll(specification, pageable);
        PageResponse<Task> pageResponse = new PageResponse<>();
        pageResponse.setPage(PageUtil.convertRawPageToPageDomain(taskEntityPage));
        pageResponse.setContent(taskMapper.toDtoList(taskEntityPage.getContent()));
        return pageResponse;
    }

    /**
     * Finds a single task by its unique ID.
     * Throws ResourceAccessException if the ID does not exist in the database.
     */
    public Task getTaskById(Long id) {
        Optional<TaskEntity> taskEntity = taskRepository.findById(id);
        if (taskEntity.isEmpty()) {
            throw new ResourceNotFoundException("KAIRO_404");
        }
        return taskMapper.toDto(taskEntity.get());
    }

    /**
     * Persists a new task into the database.
     * Maps the incoming DTO to an entity, saves it, and returns the saved version as a DTO.
     */
    public Task createTask(Task taskReq) {
        if (taskReq.getPriority() == null) {
            taskReq.setPriority(TaskPriority.LOW);
        }
        TaskEntity taskEntity = taskMapper.toEntity(taskReq);
        TaskEntity taskRes = taskRepository.save(taskEntity);
        return taskMapper.toDto(taskRes);
    }

    /**
     * Updates an existing task by ID.
     * Replaces existing entity data with the provided task object.
     */
    public Task updateTask(Task task, Long id) {
        Optional<TaskEntity> taskEntityOptional = taskRepository.findById(id);
        if (taskEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("KAIRO_404");
        }
        TaskEntity taskEntity = taskMapper.toEntity(task);
        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    /**
     * Updates the status of a specific task based on state transition rules.
     * * Workflow:
     * 1. Check for existence: Throws ResourceAccessException if the ID is invalid.
     * 2. Idempotency Check: Returns early if the status is already correct or
     * if the task is already in a final 'COMPLETED' state.
     * 3. Progression: Allows transition from TODO to IN_PROGRESS.
     * 4. Completion: Allows transition from either TODO or IN_PROGRESS to COMPLETED.
     * * @param statusMap Map containing the new "status" value.
     *
     * @param id The ID of the task to update.
     * @return The updated Task DTO after saving to the database.
     */
    public Task updateTaskStatus(TaskStatusRequest statusRequest, Long id) {
        // Retrieve the target status from the request body map
        String incomingStatus = statusRequest.getStatus();
        Optional<TaskEntity> taskEntityOptional = taskRepository.findById(id);

        // Fail-fast if the task does not exist
        if (taskEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("KAIRO_404");
        }

        TaskEntity taskEntity = taskEntityOptional.get();
        String currentStatus = taskEntity.getStatus().toString();

        /*
         * Valid Transition: Start Working
         * Moves a task from the backlog (TODO) to active work (IN_PROGRESS).
         */
        if (currentStatus.equals(TaskStatus.COMPLETED.getStatus())
                && incomingStatus.equals(TaskStatus.TODO.getStatus())) {
            taskEntity.setStatus(TaskStatus.TODO);
        } else if (currentStatus.equals(TaskStatus.TODO.getStatus())
                && incomingStatus.equals(TaskStatus.IN_PROGRESS.getStatus())) {
            taskEntity.setStatus(TaskStatus.IN_PROGRESS);
        } else if (currentStatus.equals(TaskStatus.IN_PROGRESS.getStatus())
                && incomingStatus.equals(TaskStatus.TODO.getStatus())) {
            taskEntity.setStatus(TaskStatus.TODO);
        }

        /*
         * Valid Transition: Finalize Task
         * This block permits "jumping" statuses—allowing a task to be marked
         * as COMPLETED whether it was currently IN_PROGRESS or still in TODO.
         */
        if ((currentStatus.equals(TaskStatus.TODO.getStatus())
                && incomingStatus.equals(TaskStatus.COMPLETED.getStatus()))
                || (currentStatus.equals(TaskStatus.IN_PROGRESS.getStatus())
                && incomingStatus.equals(TaskStatus.COMPLETED.getStatus()))) {
            taskEntity.setStatus(TaskStatus.COMPLETED);
        }

        // Save the updated entity and return the mapped DTO
        return taskMapper.toDto(taskRepository.save(taskEntity));
    }

    /**
     * Deletes a task from the database.
     * Verifies existence before attempting deletion to prevent silent failures.
     */
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("KAIRO_404");
        }
        taskRepository.deleteById(id);
    }


    public Task updateTaskDueDate(TaskStatusRequest task, Long id) {
        Optional<TaskEntity> taskEntityOptional = taskRepository.findById(id);

        // Fail-fast if the task does not exist
        if (taskEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("KAIRO_404");
        }
        if(task.getDueDate().isBefore(taskEntityOptional.get().getDueDate())){
            throw new DataValidationException("KAIRO_402");
        }
        TaskEntity taskEntity = taskEntityOptional.get();
        taskEntity.setDueDate(task.getDueDate());
        return taskMapper.toDto(taskRepository.save(taskEntity));
    }
}