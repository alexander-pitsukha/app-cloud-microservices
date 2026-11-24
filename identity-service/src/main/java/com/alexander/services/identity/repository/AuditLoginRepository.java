package com.alexander.services.identity.repository;

import com.alexander.services.identity.entity.AuditLogin;
import com.alexander.services.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuditLoginRepository extends JpaRepository<AuditLogin, Long> {

    @Query("select a from AuditLogin a order by a.user, a.createDate desc")
    Page<AuditLogin> findByLastCreatedDate(Pageable pageable);

    @Query("select a from AuditLogin a where a.createDate = (select max(a1.createDate) from AuditLogin a1 where a1.user = a.user)")
    List<AuditLogin> findByLastCreatedDate();

    @Query("select a from AuditLogin a where a.user.id = :userId order by a.createDate desc")
    List<AuditLogin> findByUserId(Long userId);

    @Query("select a from AuditLogin a where a.user = :user and a.createDate = (select max(a1.createDate) from AuditLogin a1 where a1.user = :user)")
    Optional<AuditLogin> findByLastCreatedDate(User user);

    @Modifying
    @Query("delete from AuditLogin a where a.createDate < :localDateTime")
    void deleteByCreateDate(LocalDateTime localDateTime);

    @Modifying
    @Query("delete from AuditLogin a where a.user.id = :userId")
    void deleteByUserId(Long userId);

}
