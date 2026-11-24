package com.alexander.services.identity.service.impl;

import com.alexander.services.identity.converter.RoleMapper;
import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.entity.Role;
import com.alexander.services.identity.repository.RoleRepository;
import com.alexander.services.identity.service.RoleService;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.service.ServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RoleServiceImpl extends ServiceImpl<RoleRepository, RoleMapper, Role, RoleDto> implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        super(roleRepository, roleMapper);
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "name";
        Page<Role> roles;
        if (!ObjectUtils.isEmpty(query)) {
            roles = roleRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            roles = roleRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return roles.map(role -> new AutocompleteData(role.getId(), role.getName())).toList();
    }

    @Transactional
    @Override
    public RoleDto update(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id).orElseThrow();
        role.setName(roleDto.getName());
        return roleMapper.toDto(role);
    }

}
