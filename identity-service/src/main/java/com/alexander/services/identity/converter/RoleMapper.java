package com.alexander.services.identity.converter;

import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.entity.Role;
import com.alexander.services.identity.entity.User;
import com.alexander.services.library.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper extends BaseMapper<Role, RoleDto> {

    @Mapping(target = "id", source = "role.id")
    RoleDto toDto(Role role);

    @Mapping(target = "role", ignore = true)
    UserDto toDto(User user);

    @Mapping(target = "users", ignore = true)
    Role toEntity(RoleDto roleDto);

}
