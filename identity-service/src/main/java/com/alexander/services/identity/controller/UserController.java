package com.alexander.services.identity.controller;

import com.alexander.services.identity.controller.request.UserRequest;
import com.alexander.services.identity.dto.AuditLoginDto;
import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.dto.UserDto;
import com.alexander.services.identity.service.UserService;
import com.alexander.services.library.dto.AutocompleteData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    @PreAuthorize("@userSecurity.hasUserId(authentication, #id) or hasAuthority('ADMIN')")
    public ResponseEntity<RoleDto> getUserRole(@PathVariable Long id) {
        log.info("User role get: id={}", id);
        return ResponseEntity.ok(userService.findUserRole(id));
    }

    @GetMapping("audit-logins")
    public ResponseEntity<Page<AuditLoginDto>> getUserAuditLogins(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("Users audit logins get");
        return ResponseEntity.ok(userService.findUserAuditLogins(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("last-audit-login")
    public ResponseEntity<List<AuditLoginDto>> getUserLastAuditLogins() {
        log.info("Users last audit login get");
        return ResponseEntity.ok(userService.findUserAuditLogins());
    }

    @GetMapping("{id}/audit-logins")
    public ResponseEntity<List<AuditLoginDto>> getUserAuditLogins(@PathVariable Long id) {
        log.info("User audit logins get: id={}", id);
        return ResponseEntity.ok(userService.findUserAuditLogins(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> save(@RequestBody @Validated UserRequest userRequest) {
        log.info("User save: {}", userRequest);
        var dto = userService.save(userRequest);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri()).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserDto> update(@PathVariable Long id, @RequestBody @Validated UserDto userDto) {
        log.info("User update id={}: {}", id, userDto);
        return ResponseEntity.accepted().body(userService.update(id, userDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("User delete: id={}", id);
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
