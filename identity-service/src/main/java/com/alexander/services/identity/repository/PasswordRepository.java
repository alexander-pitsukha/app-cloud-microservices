package com.alexander.services.identity.repository;

import com.alexander.services.identity.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Long> {

    @Modifying
    @Query("delete from Password p where p.user.id = :userId")
    void deleteByUserId(Long userId);

    @Modifying
    @Query("delete from Password p where p.createDate < :localDateTime and (select count(p1) from Password p1 where p1.user.id = p.user.id and p1.createDate > p.createDate) > :uniqueLimit")
    void deleteByCreateDate(LocalDateTime localDateTime, int uniqueLimit);

}
