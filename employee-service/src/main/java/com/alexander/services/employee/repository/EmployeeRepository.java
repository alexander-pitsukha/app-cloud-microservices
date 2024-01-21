package com.alexander.services.employee.repository;

import com.alexander.services.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e from Employee e where e.departmentId = :departmentId")
    List<Employee> findByDepartment(@Param("departmentId") Long departmentId);

    @Query("select e from Employee e where e.organizationId = :organizationId")
    List<Employee> findByOrganization(@Param("organizationId") Long organizationId);

}
