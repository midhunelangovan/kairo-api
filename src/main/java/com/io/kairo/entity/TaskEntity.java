package com.io.kairo.entity;

import com.io.kairo.enums.TaskPriority;
import com.io.kairo.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import kals.com.core.entity.AuditEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.time.LocalDate;

@Entity
@Table(name = "task")
@Getter
@Setter
@SoftDelete
public class TaskEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;
    @Enumerated(value = EnumType.STRING)
    private TaskPriority priority;
    private LocalDate dueDate;

    @PrePersist
    public void prePersist(){
        if(dueDate == null){
            dueDate = LocalDate.now();
        }
    }

}
