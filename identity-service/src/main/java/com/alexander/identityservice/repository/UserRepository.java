package com.alexander.identityservice.repository;

import com.alexander.identityservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name like ?1%")
    Page<User> findAllByNameLikeAsc(String query, Pageable pageable);

    @Query("select u from User u where u.email = :email")
    User findByEmail(@Param("email") String email);

}
