package com.alexander.services.identity.converter;

import com.alexander.services.identity.dto.AuditLoginDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.entity.AuditLogin;
import com.alexander.services.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuditLoginMapper {

    List<AuditLoginDto> toDtos(List<AuditLogin> auditLogins);

    @Mapping(target = "id", source = "user.id")
    AuditLoginDto toDto(AuditLogin auditLogin);

    @Mapping(target = "role", ignore = true)
    UserDto toDto(User user);

}
