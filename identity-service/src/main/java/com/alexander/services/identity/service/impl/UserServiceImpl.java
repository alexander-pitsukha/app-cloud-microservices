package com.alexander.services.identity.service.impl;

import com.alexander.services.identity.controller.request.UserRequest;
import com.alexander.services.identity.converter.AuditLoginMapper;
import com.alexander.services.identity.converter.RoleMapper;
import com.alexander.services.identity.converter.UserMapper;
import com.alexander.services.identity.dto.AuditLoginDto;
import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.entity.AuditLogin;
import com.alexander.services.identity.entity.Password;
import com.alexander.services.identity.entity.Role;
import com.alexander.services.identity.entity.User;
import com.alexander.services.identity.exception.PasswordAlreadyUsedException;
import com.alexander.services.identity.exception.ValidationException;
import com.alexander.services.identity.repository.*;
import com.alexander.services.identity.service.EmailService;
import com.alexander.services.identity.service.UserService;
import com.alexander.services.identity.util.Constants;
import com.alexander.services.identity.util.MessageProvider;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.service.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserRepository, UserMapper, User, UserDto> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final AuditLoginMapper auditLoginMapper;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordRepository passwordRepository;
    private final AuditLoginRepository auditLoginRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final MessageProvider messageProvider;
    private final Integer periodVerification;
    private final Integer periodReset;
    private final Integer uniqueLimit;
    private final Integer passwordDeleteMonths;
    private final Integer auditDeleteMonths;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleMapper roleMapper, AuditLoginMapper auditLoginMapper, RoleRepository roleRepository,
                           RefreshTokenRepository refreshTokenRepository, PasswordRepository passwordRepository, AuditLoginRepository auditLoginRepository, PasswordEncoder passwordEncoder,
                           EmailService emailService, MessageProvider messageProvider, @Value("${email.verification.token.expiration.hours:3}") Integer periodVerification,
                           @Value("${password.reset.token.expiration.hours:3}") Integer periodReset, @Value("${password.unique.limit:5}") Integer uniqueLimit,
                           @Value("${password.delete.months:6}") Integer passwordDeleteMonths, @Value("${audit.delete.months:6}") Integer auditDeleteMonths) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.auditLoginMapper = auditLoginMapper;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordRepository = passwordRepository;
        this.auditLoginRepository = auditLoginRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.messageProvider = messageProvider;
        this.periodVerification = periodVerification;
        this.periodReset = periodReset;
        this.uniqueLimit = uniqueLimit;
        this.passwordDeleteMonths = passwordDeleteMonths;
        this.auditDeleteMonths = auditDeleteMonths;
    }

    @Scheduled(cron = "0 0 0 1 1,7 *")
    @Transactional
    public void deleteExpiredPasswordHistory() {
        var localDateTime = LocalDateTime.now(ZoneId.systemDefault()).minusMonths(passwordDeleteMonths);
        log.info("Delete passwords by create date {} and less then {} month", localDateTime, uniqueLimit);
        passwordRepository.deleteByCreateDate(localDateTime, uniqueLimit);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteRefreshTokensByExpireDate() {
        var now = LocalDateTime.now(ZoneId.systemDefault());
        log.info("Delete refreshTokens by expired date {}", now);
        var localDateTime = LocalDateTime.now(ZoneId.systemDefault()).minusMonths(auditDeleteMonths);
        auditLoginRepository.deleteByCreateDate(localDateTime);
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "username";
        Page<User> users;
        if (!ObjectUtils.isEmpty(query)) {
            users = userRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            users = userRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return users.map(user -> new AutocompleteData(user.getId(), user.getUsername())).toList();
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        return userMapper.toDto(user);
    }

    @Override
    public RoleDto findUserRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return roleMapper.toDto(user.getRole());
    }

    @Override
    public Page<AuditLoginDto> findUserAuditLogins(int page, int size) {
        Page<AuditLogin> auditLogins = auditLoginRepository.findByLastCreatedDate(page == 0 && size == 0 ? Pageable.unpaged() : PageRequest.of(page, size));
        return auditLogins.map(auditLoginMapper::toDto);
    }

    @Override
    public List<AuditLoginDto> findUserAuditLogins() {
        List<AuditLogin> auditLogins = auditLoginRepository.findByLastCreatedDate();
        return auditLoginMapper.toDtos(auditLogins);
    }

    @Override
    public List<AuditLoginDto> findUserAuditLogins(Long userId) {
        List<AuditLogin> auditLogins = auditLoginRepository.findByUserId(userId);
        return auditLoginMapper.toDtos(auditLogins);
    }

    @Transactional
    @Override
    public UserDto userSignIn(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        if (user.getPasswordResetTokenExpiresAt() != null) {
            throw new ValidationException(messageProvider.getMessage("error.msg.user.password.reset.not.updated"));
        }
        var auditLogin = new AuditLogin();
        auditLogin.setUser(user);
        auditLoginRepository.save(auditLogin);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void userLogOut(String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        auditLoginRepository.findByLastCreatedDate(user).ifPresentOrElse(auditLogin -> auditLogin.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault())),
                () -> {
                    AuditLogin auditLogin = new AuditLogin();
                    auditLogin.setUser(user);
                    auditLogin.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()));
                    auditLoginRepository.save(auditLogin);
                });
    }

    @Override
    public UserDto save(UserDto userDto) {
        throw new UnsupportedOperationException();
    }

    @Transactional
    @Override
    public UserDto save(UserRequest userRequest) {
        var token = UUID.randomUUID();
        User user = userMapper.toEntity(userRequest);
        Role role = roleRepository.findById(userRequest.getRoleId()).orElseThrow();
        user.setEmailVerified(Boolean.FALSE);
        user.setEmailVerificationToken(token.toString());
        user.setEmailVerificationTokenExpiresAt(LocalDateTime.now(ZoneId.systemDefault()).plusHours(periodVerification));
        user.setRole(role);
        user = userRepository.save(user);
        var password = new Password();
        password.setUser(user);
        password.setPasswordHash(passwordEncoder.encode(userRequest.getPassword()));
        password = passwordRepository.save(password);
        userRepository.updateLastUserPassword(user.getId(), password);
        emailService.sendEmailForCreateUser(user.getEmail(), token);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto update(Long id, UserDto userDto) {
        var token = UUID.randomUUID();
        User user = userRepository.findById(id).orElseThrow();
        Role role = roleRepository.findById(userDto.getRole().getId()).orElseThrow();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setEmailVerified(Boolean.FALSE);
        user.setEmailVerificationToken(token.toString());
        user.setEmailVerificationTokenExpiresAt(LocalDateTime.now(ZoneId.systemDefault()).plusHours(periodVerification));
        user.setRole(role);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void updateEmailVerification(String token) {
        var user = userRepository.findByEmailVerificationToken(token).orElseThrow();
        if (LocalDateTime.now(ZoneId.systemDefault()).isAfter(user.getEmailVerificationTokenExpiresAt())) {
            throw new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_USER_EMAIL_VERIFICATION_RESET_OR_EXPIRED));
        }
        userRepository.updateEmailVerificationToken(token);
    }

    @Transactional
    @Override
    public UserDto updatePasswordAndSendTokenForReset(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow();
        if (ObjectUtils.isEmpty(password)) {
            throw new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_AUTH_WRONG_PASSWORD));
        } else if (!user.getPasswords().isEmpty() && isPasswordRecentlyUsed(user, password)) {
            throw new PasswordAlreadyUsedException(messageProvider.getMessage(Constants.ERROR_MSG_USER_PASSWORD_UPDATE));
        }
        var token = UUID.randomUUID();
        user.setPasswordResetToken(token.toString());
        user.setPasswordResetTokenExpiresAt(LocalDateTime.now(ZoneId.systemDefault()).plusHours(periodReset));
        var entry = new Password();
        entry.setPasswordHash(passwordEncoder.encode(password));
        entry.setUser(user);
        entry = passwordRepository.save(entry);
        user.setPassword(entry);
        userRepository.save(user);
        emailService.sendEmailForUpdateUserPassword(user.getEmail(), token, password);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUserPasswordByPasswordResetToken(String token, String currentPassword, String newPassword) {
        var user = userRepository.findByPasswordResetToken(token).orElseThrow(() -> new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_USER_PASSWORD_RESET_OR_EXPIRED)));
        if (LocalDateTime.now(ZoneId.systemDefault()).isAfter(user.getPasswordResetTokenExpiresAt())) {
            throw new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_USER_PASSWORD_RESET_OR_EXPIRED));
        } else if (!passwordEncoder.matches(currentPassword, user.getPassword().getPasswordHash())) {
            throw new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_AUTH_WRONG_PASSWORD));
        } else if (!user.getPasswords().isEmpty() && isPasswordRecentlyUsed(user, newPassword)) {
            throw new PasswordAlreadyUsedException(messageProvider.getMessage(Constants.ERROR_MSG_USER_PASSWORD_UPDATE));
        }
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        var entry = new Password();
        entry.setUser(user);
        entry.setPasswordHash(passwordEncoder.encode(newPassword));
        passwordRepository.save(entry);
        userRepository.updateLastUserPassword(user.getId(), entry);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public UserDto updatePassword(Long id, String currentPassword, String newPassword) {
        User user = userRepository.findById(id).orElseThrow();
        if (!passwordEncoder.matches(currentPassword, user.getPassword().getPasswordHash())) {
            throw new ValidationException(messageProvider.getMessage(Constants.ERROR_MSG_AUTH_WRONG_PASSWORD));
        }
        if (!user.getPasswords().isEmpty() && isPasswordRecentlyUsed(user, newPassword)) {
            throw new PasswordAlreadyUsedException(messageProvider.getMessage(Constants.ERROR_MSG_USER_PASSWORD_UPDATE));
        }
        var entry = new Password();
        entry.setPasswordHash(passwordEncoder.encode(newPassword));
        entry.setUser(user);
        entry = passwordRepository.save(entry);
        userRepository.updateLastUserPassword(user.getId(), entry);
        return userMapper.toDto(user);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        refreshTokenRepository.deleteByUserId(id);
        passwordRepository.deleteByUserId(id);
        auditLoginRepository.deleteByUserId(id);
        userRepository.deleteById(id);
    }

    private boolean isPasswordRecentlyUsed(User user, String rawPassword) {
        return user.getPasswords().stream().sorted(Comparator.comparing(Password::getCreateDate, Comparator.nullsLast(Comparator.reverseOrder()))).limit(uniqueLimit)
                .anyMatch(password -> passwordEncoder.matches(rawPassword, password.getPasswordHash()));
    }

}
