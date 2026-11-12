package com.alexander.services.organization.repository;

import com.alexander.services.organization.model.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query("select o from Organization o where o.name like ?1%")
    Page<Organization> findAllByNameLikeAsc(String query, Pageable pageable);

}
