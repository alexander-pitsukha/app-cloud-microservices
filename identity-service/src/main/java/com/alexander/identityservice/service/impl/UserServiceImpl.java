package com.alexander.identityservice.service.impl;

import com.alexander.dto.AutocompleteData;
import com.alexander.enums.RoleType;
import com.alexander.identityservice.controller.request.UserBody;
import com.alexander.identityservice.dto.UserDto;
import com.alexander.identityservice.model.RefreshToken;
import com.alexander.identityservice.model.User;
import com.alexander.identityservice.repository.RefreshTokenRepository;
import com.alexander.identityservice.repository.UserRepository;
import com.alexander.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DefaultConversionService defaultConversionService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Page<UserDto> findAll(int page, int size) {
        Page<User> users;
        if (page == 0 && size == 0) {
            users = userRepository.findAll(Pageable.unpaged());
        } else {
            users = userRepository.findAll(PageRequest.of(page, size));
        }
        return users.map(user -> defaultConversionService.convert(user, UserDto.class));
    }

    @Override
    public List<AutocompleteData> findAutocomplete(String query, Integer limit) {
        final String fieldName = "name";
        Page<User> users;
        if (!ObjectUtils.isEmpty(query)) {
            users = userRepository.findAllByNameLikeAsc(query, PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        } else {
            users = userRepository.findAll(PageRequest.of(0, limit, Sort.by(fieldName).ascending()));
        }
        return users.map(user -> new AutocompleteData(user.getId(), user.getName())).toList();
    }

    @Override
    public UserDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return defaultConversionService.convert(user, UserDto.class);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return defaultConversionService.convert(user, UserDto.class);
    }

    @Override
    public RoleType findUserRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return user.getRole();
    }

    @Transactional
    @Override
    public UserDto save(UserDto userDto) {
        User user = defaultConversionService.convert(userDto, User.class);
        user = userRepository.save(user);
        return defaultConversionService.convert(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto save(UserBody userBody) {
        User user = defaultConversionService.convert(userBody, User.class);
        user = userRepository.save(user);
        return defaultConversionService.convert(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto update(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setRole(userDto.getRole());
        return defaultConversionService.convert(user, UserDto.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(id);
        if (optionalRefreshToken.isPresent()) {
            refreshTokenRepository.deleteByUserId(id);
        }
        userRepository.deleteById(id);
    }

}
