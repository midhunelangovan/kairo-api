package com.io.kairo.mapper;

import com.io.kairo.entity.TaskEntity;
import com.io.kairo.model.Task;
import kals.com.core.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper extends BaseMapper<TaskEntity, Task> {
}
