package org.example.tasks.mapper.v1;

import org.example.tasks.dao.entity.Task;
import org.example.tasks.web.v1.dto.TaskResponse;
import org.example.tasks.web.v1.dto.TaskUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {
  TaskResponse taskToTaskResponse(Task task);

  Task requestToTask(TaskUpsertRequest request);
}
