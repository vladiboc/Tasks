package org.example.tasks.mapper.v1;

import org.example.tasks.dao.entity.User;
import org.example.tasks.web.v1.dto.UserResponse;
import org.example.tasks.web.v1.dto.UserUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  User requstToUser(UserUpsertRequest request);

  UserResponse userToResponse(User user);
}
