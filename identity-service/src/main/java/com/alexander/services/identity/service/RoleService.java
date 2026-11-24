package com.alexander.services.identity.service;

import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService extends Service<RoleDto, Page<RoleDto>, List<AutocompleteData>, Long> {
}
