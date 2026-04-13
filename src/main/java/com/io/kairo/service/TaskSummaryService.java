package com.io.kairo.service;

import com.io.kairo.entity.TaskEntity;
import com.io.kairo.enums.TaskStatus;
import com.io.kairo.mapper.TaskMapper;
import com.io.kairo.model.Task;
import com.io.kairo.repository.TaskRepository;
import kals.com.core.model.PageResponse;
import kals.com.core.utility.PageUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service class responsible for retrieving filtered summaries of tasks,
 * such as current day tasks and overdue tasks.
 */
@Service
public class TaskSummaryService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskSummaryService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    /**
     * Retrieves a paginated list of tasks due today that are not yet completed.
     * * @param pageable Pagination and sorting information
     *
     * @return PageResponse containing a list of Task DTOs and metadata
     */
    public PageResponse<Task> getAllTodayTask(Pageable pageable) {
        Page<TaskEntity> taskEntityList = taskRepository.getAllByDueDateAndStatusNot(
                LocalDate.now(),
                TaskStatus.COMPLETED,
                pageable
        );

        PageResponse<Task> pageResponse = new PageResponse<>();
        pageResponse.setPage(PageUtil.convertRawPageToPageDomain(taskEntityList));
        pageResponse.setContent(taskMapper.toDtoList(taskEntityList.getContent()));

        return pageResponse;
    }

    /**
     * Retrieves a paginated list of tasks that were due before today and are not yet completed.
     * * @param pageable Pagination and sorting information
     *
     * @return PageResponse containing a list of overdue Task DTOs and metadata
     */
    public PageResponse<Task> getAllOverdueTaskList(Pageable pageable) {
        Page<TaskEntity> taskEntityList = taskRepository.getAllByDueDateLessThanAndStatusNot(
                LocalDate.now(),
                TaskStatus.COMPLETED,
                pageable
        );

        PageResponse<Task> pageResponse = new PageResponse<>();
        pageResponse.setPage(PageUtil.convertRawPageToPageDomain(taskEntityList));
        pageResponse.setContent(taskMapper.toDtoList(taskEntityList.getContent()));

        return pageResponse;
    }
}