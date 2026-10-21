package com.alexander.services.organization.client;

import com.alexander.dto.DepartmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "department-service")
public interface DepartmentClient {

    @GetMapping("/organization/{organizationId}")
    ResponseEntity<List<DepartmentDto>> findByOrganization(@PathVariable("organizationId") Long organizationId);

    @GetMapping("/organization/{organizationId}/with-employees")
    ResponseEntity<List<DepartmentDto>> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId);

}
