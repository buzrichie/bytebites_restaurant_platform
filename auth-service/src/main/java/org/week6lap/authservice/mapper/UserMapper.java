package org.week6lap.authservice.mapper;

import org.mapstruct.*;
import org.week6lap.authservice.dto.UserRecord;
import org.week6lap.authservice.dto.UserResponse;
import org.week6lap.authservice.model.User;

@Mapper
public interface UserMapper {

    // Entity → Full DTO (with password)
    UserRecord toRecord(User user);

    // Full DTO (with password) → Entity
    User toEntity(UserRecord record);

    // Entity → Safe Response DTO (no password)
    UserResponse toResponse(User user);
}

