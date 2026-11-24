package com.alexander.services.department.service;

import com.alexander.services.department.entity.Department;
import com.alexander.services.department.mapper.DepartmentMapper;
import com.alexander.services.department.repository.DepartmentRepository;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.dto.DepartmentDto;
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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentRepository, DepartmentMapper, Department, DepartmentDto> implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, DepartmentMapper departmentMapper) {
        super(departmentRepository, departmentMapper);
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "name";
        Page<Department> departments;
        if (!ObjectUtils.isEmpty(query)) {
            departments = departmentRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            departments = departmentRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return departments.map(department -> new AutocompleteData(department.getId(), department.getName())).toList();
    }

    @Override
    public List<DepartmentDto> findByOrganization(Long organizationId) {
        List<Department> departments = departmentRepository.findByOrganization(organizationId);
        return departmentMapper.toDtos(departments);
    }

    @Override
    public List<DepartmentDto> findByOrganizationWithEmployees(Long organizationId) {
        List<Department> departments = departmentRepository.findByOrganization(organizationId);
        return departmentMapper.toDtos(departments);
    }

    @Transactional
    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        var department = departmentRepository.findById(id).orElseThrow();
        department.setName(departmentDto.getName());
        department.setOrganizationId(departmentDto.getOrganizationId());
        return departmentMapper.toDto(department);
    }

}
