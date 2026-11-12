package com.alexander.services.department.repository;

import com.alexander.services.department.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("select d from Department d where d.name like ?1%")
    Page<Department> findAllByNameLikeAsc(String query, Pageable pageable);

    @Query("select d from Department d where d.organizationId = :organizationId")
    List<Department> findByOrganization(@Param("organizationId") Long organizationId);

}
