package com.alexander.services.department.client;

import com.alexander.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "employee-service")
public interface EmployeeClient {

    @GetMapping("department/{departmentId}")
    ResponseEntity<List<EmployeeDto>> findByDepartment(@PathVariable("departmentId") Long departmentId);

}
