package com.alexander.services.employee.service;

import com.alexander.dto.AutocompleteData;
import com.alexander.dto.EmployeeDto;
import com.alexander.services.employee.mapper.EmployeeMapper;
import com.alexander.services.employee.model.Employee;
import com.alexander.services.employee.repository.EmployeeRepository;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Page<EmployeeDto> findAll(int page, int size) {
        Page<Employee> employees;
        if (page == 0 && size == 0) {
            employees = employeeRepository.findAll(Pageable.unpaged());
        } else {
            employees = employeeRepository.findAll(PageRequest.of(page, size));
        }
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "name";
        Page<Employee> employees;
        if (!ObjectUtils.isEmpty(query)) {
            employees = employeeRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            employees = employeeRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return employees.map(employee -> new AutocompleteData(employee.getId(), employee.getName())).toList();
    }

    @Override
    public EmployeeDto findById(Long id) {
        var profile = employeeRepository.findById(id).orElseThrow();
        return employeeMapper.toDto(profile);
    }

    @Override
    public List<EmployeeDto> findByDepartment(Long departmentId) {
        List<Employee> employees = employeeRepository.findByDepartment(departmentId);
        return employeeMapper.toDtos(employees);
    }

    @Override
    public List<EmployeeDto> findByOrganization(Long organizationId) {
        List<Employee> employees = employeeRepository.findByOrganization(organizationId);
        return employeeMapper.toDtos(employees);
    }

    @Transactional
    @Override
    public EmployeeDto save(EmployeeDto employeeDto) {
        var employee = employeeMapper.toEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        var employee = employeeRepository.findById(id).orElseThrow();
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setPosition(employeeDto.getPosition());
        employee.setOrganizationId(employeeDto.getOrganizationId());
        employee.setDepartmentId(employeeDto.getDepartmentId());
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

}
