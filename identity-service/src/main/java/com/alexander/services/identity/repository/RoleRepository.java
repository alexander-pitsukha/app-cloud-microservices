package com.alexander.services.identity.repository;

import com.alexander.services.identity.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.name like ?1%")
    Page<Role> findAllByNameLikeAsc(String query, Pageable pageable);

}
