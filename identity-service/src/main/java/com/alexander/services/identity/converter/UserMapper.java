package com.alexander.services.identity.converter;

import com.alexander.services.identity.controller.request.UserRequest;
import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.entity.Role;
import com.alexander.services.identity.entity.User;
import com.alexander.services.library.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Mapping(target = "id", source = "user.id")
    UserDto toDto(User user);

    @Mapping(target = "users", ignore = true)
    RoleDto toDto(Role role);

    User toEntity(UserDto userDto);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserRequest userRequest);

}
