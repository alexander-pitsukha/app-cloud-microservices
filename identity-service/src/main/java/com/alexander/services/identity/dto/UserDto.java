package com.alexander.services.identity.dto;

import com.alexander.services.library.dto.AbstractLongIdentifiableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends AbstractLongIdentifiableDto {

    private String username;

    private String email;

    private RoleDto role;

}
