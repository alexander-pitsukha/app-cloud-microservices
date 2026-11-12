package com.alexander.identityservice.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.enums.RoleType;
import com.alexander.identityservice.controller.request.UserBody;
import com.alexander.identityservice.dto.UserDto;
import com.alexander.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService extends Service<UserDto, Page<UserDto>, List<AutocompleteData>, Long> {

    UserDto findUserByEmail(String email);

    RoleType findUserRole(Long userId);

    UserDto save(UserBody userBody);

}
