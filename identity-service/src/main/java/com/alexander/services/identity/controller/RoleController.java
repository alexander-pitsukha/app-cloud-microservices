package com.alexander.services.identity.controller;

import com.alexander.services.identity.dto.RoleDto;
import com.alexander.services.identity.service.RoleService;
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
@RequestMapping("roles")
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<Page<RoleDto>> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        log.info("Role get");
        return ResponseEntity.ok(roleService.findAll(page != null ? page : 0, size != null ? size : 0));
    }

    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getAutocomplete(@RequestParam String query, @RequestParam Integer limit) {
        log.info("Role get (autocomplete).");
        return ResponseEntity.ok(roleService.findAutocomplete(query, limit));
    }

    @GetMapping("{id}")
    public ResponseEntity<RoleDto> getById(@PathVariable Long id) {
        log.info("Role get: id={}", id);
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RoleDto> save(@RequestBody @Validated RoleDto roleDto) {
        log.info("Role save: {}", roleDto);
        var dto = roleService.save(roleDto);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri()).body(dto);
    }

    @PutMapping("{id}")
    public ResponseEntity<RoleDto> update(@PathVariable Long id, @RequestBody @Validated RoleDto roleDto) {
        log.info("Role update id={}: {}", id, roleDto);
        return ResponseEntity.accepted().body(roleService.update(id, roleDto));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        log.info("User delete: id={}", id);
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
