package com.alexander.services.employee.service;

import com.alexander.dto.EmployeeDto;
import com.alexander.services.employee.mapper.EmployeeMapper;
import com.alexander.services.employee.model.Employee;
import com.alexander.services.employee.repository.EmployeeRepository;
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
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeDto> findAll(int page, int size) {
        Stream<Employee> stream;
        if (page == 0 && size == 0) {
            List<Employee> employees = employeeRepository.findAll();
            stream = employees.stream();
        } else {
            Pageable pageable = PageRequest.of(page, size);
            Page<Employee> employees = employeeRepository.findAll(pageable);
            stream = employees.stream();
        }
        return stream.map(employeeMapper::toDto).toList();
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
    public EmployeeDto add(EmployeeDto employeeDto) {
        var employee = employeeMapper.toEntity(employeeDto);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public EmployeeDto update(Long id, EmployeeDto employeeDto) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        var employee = employeeRepository.getById(id);
        employee.setName(employeeDto.getName());
        employee.setAge(employeeDto.getAge());
        employee.setPosition(employeeDto.getPosition());
        employee.setOrganizationId(employeeDto.getOrganizationId());
        employee.setDepartmentId(employeeDto.getDepartmentId());
        return employeeMapper.toDto(employee);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new IllegalArgumentException();
        }
        employeeRepository.deleteById(id);
    }

}
