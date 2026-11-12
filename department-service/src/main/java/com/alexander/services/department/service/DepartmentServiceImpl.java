package com.alexander.services.department.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.DepartmentDto;
import com.alexander.services.department.mapper.DepartmentMapper;
import com.alexander.services.department.model.Department;
import com.alexander.services.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Page<DepartmentDto> findAll(int page, int size) {
        Page<Department> departments;
        if (page == 0 && size == 0) {
            departments = departmentRepository.findAll(Pageable.unpaged());
        } else {
            departments = departmentRepository.findAll(PageRequest.of(page, size));
        }
        return departments.map(departmentMapper::toDto);
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
    public DepartmentDto findById(Long id) {
        var department = departmentRepository.findById(id).orElseThrow();
        return departmentMapper.toDto(department);
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
    public DepartmentDto save(DepartmentDto departmentDto) {
        var department = departmentMapper.toEntity(departmentDto);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Transactional
    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        var department = departmentRepository.findById(id).orElseThrow();
        department.setName(departmentDto.getName());
        department.setOrganizationId(departmentDto.getOrganizationId());
        return departmentMapper.toDto(department);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

}
