package com.alexander.identityservice.controller;

import com.alexander.dto.AutocompleteData;
import com.alexander.enums.RoleType;
import com.alexander.identityservice.dto.UserDto;
import com.alexander.identityservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("User get");
        return ResponseEntity.ok(userService.findAll(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getAutocomplete(@RequestParam String query, @RequestParam Integer limit) {
        log.info("User get (autocomplete).");
        return ResponseEntity.ok(userService.findAutocomplete(query, limit));
    }

    @GetMapping("{id}/role")
    public ResponseEntity<RoleType> getUserRole(@PathVariable Long id) {
        log.info("User role get: id={}", id);
        return ResponseEntity.ok(userService.findUserRole(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("User delete: id={}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
