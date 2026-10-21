package com.alexander.services.department.service;

import com.alexander.dto.DepartmentDto;
import com.alexander.services.department.mapper.DepartmentMapper;
import com.alexander.services.department.model.Department;
import com.alexander.services.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDto> findAll(int page, int size) {
        Stream<Department> stream;
        if (page == 0 && size == 0) {
            List<Department> departments = departmentRepository.findAll();
            stream = departments.stream();
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Department> departments = departmentRepository.findAll(pageable);
            stream = departments.stream();
        }
        return stream.map(departmentMapper::toDto).toList();
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
    public DepartmentDto add(DepartmentDto departmentDto) {
        var department = departmentMapper.toEntity(departmentDto);
        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    @Transactional
    @Override
    public DepartmentDto update(Long id, DepartmentDto departmentDto) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        var department = departmentRepository.getById(id);
        department.setName(departmentDto.getName());
        department.setOrganizationId(departmentDto.getOrganizationId());
        return departmentMapper.toDto(department);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        departmentRepository.deleteById(id);
    }
}
