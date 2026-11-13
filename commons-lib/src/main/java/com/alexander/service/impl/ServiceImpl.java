package com.alexander.service.impl;

import com.alexander.domain.AbstractIdentifiable;
import com.alexander.dto.AbstractLongIdentifiableDto;
import com.alexander.dto.AutocompleteData;
import com.alexander.mapper.BaseMapper;
import com.alexander.service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class ServiceImpl<R extends JpaRepository<E, Long>, M extends BaseMapper<E, D>, E extends AbstractIdentifiable<Long>, D extends AbstractLongIdentifiableDto> implements Service<D, Page<D>, List<AutocompleteData>, Long> {

    private final R repository;
    private final M mapper;

    @Override
    public Page<D> findAll(int page, int size) {
        Page<E> entities;
        if (page == 0 && size == 0) {
            entities = repository.findAll(Pageable.unpaged());
        } else {
            entities = repository.findAll(PageRequest.of(page, size));
        }
        return entities.map(mapper::toDto);
    }

    @Override
    public D findById(Long id) {
        var entity = repository.findById(id).orElseThrow();
        return mapper.toDto(entity);
    }

    @Transactional
    @Override
    public D save(D dto) {
        var entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
