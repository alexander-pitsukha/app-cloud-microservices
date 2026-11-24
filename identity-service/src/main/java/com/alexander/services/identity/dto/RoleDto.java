package com.alexander.services.identity.dto;

import com.alexander.services.library.dto.AbstractLongIdentifiableDto;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoleDto extends AbstractLongIdentifiableDto {

    private String name;

    List<UserDto> users = new ArrayList<>();

}
