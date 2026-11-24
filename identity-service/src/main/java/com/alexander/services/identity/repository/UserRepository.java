package com.alexander.services.identity.repository;

import com.alexander.services.identity.entity.Password;
import com.alexander.services.identity.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username like ?1%")
    Page<User> findAllByNameLikeAsc(String query, Pageable pageable);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = :email and u.emailVerified = true")
    Optional<User> findByEmailAndEmailVerifiedIsTrue(String email);

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(String username);

    @Query("select u from User u where u.emailVerificationToken = :emailVerificationToken")
    Optional<User> findByEmailVerificationToken(String emailVerificationToken);

    @Query("select u from User u where u.passwordResetToken = :passwordResetToken")
    Optional<User> findByPasswordResetToken(String passwordResetToken);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.password = :password where u.id = :id")
    void updateLastUserPassword(Long id, Password password);

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.emailVerified = true, u.emailVerificationToken = null, u.emailVerificationTokenExpiresAt = null where u.emailVerificationToken = :emailVerificationToken")
    void updateEmailVerificationToken(String emailVerificationToken);

}
