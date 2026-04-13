package com.io.kairo.repository;

import com.io.kairo.entity.TaskEntity;
import com.io.kairo.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    Page<TaskEntity> getAllByDueDateAndStatusNot(LocalDate dueDate, TaskStatus taskStatus, Pageable pageable);

    Page<TaskEntity> getAllByDueDateLessThanAndStatusNot(LocalDate now, TaskStatus taskStatus, Pageable pageable);

}
