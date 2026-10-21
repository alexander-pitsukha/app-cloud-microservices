package com.alexander.identityservice.controller;

import com.alexander.enums.RoleType;
import com.alexander.identityservice.dto.UserDto;
import com.alexander.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("{userId}/role")
    public ResponseEntity<RoleType> getUserRole(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserRole(userId));
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

}
