package com.alexander.services.identity.service;

import com.alexander.services.identity.controller.request.UserRequest;
import com.alexander.services.identity.dto.AuditLoginDto;
import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.service.Service;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService extends Service<UserDto, Page<UserDto>, List<AutocompleteData>, Long> {

    UserDto findUserByEmail(String email);

    UserDto findByUsername(String username);

    RoleDto findUserRole(Long userId);

    Page<AuditLoginDto> findUserAuditLogins(int page, int size);

    List<AuditLoginDto> findUserAuditLogins();

    List<AuditLoginDto> findUserAuditLogins(Long userId);

    UserDto userSignIn(String email);

    void userLogOut(String username);

    UserDto save(UserRequest userRequest);

    void updateEmailVerification(String token);

    UserDto updatePasswordAndSendTokenForReset(Long id, String password);

    UserDto updateUserPasswordByPasswordResetToken(String token, String currentPassword, String newPassword);

    UserDto updatePassword(Long id, String currentPassword, String newPassword);

}
