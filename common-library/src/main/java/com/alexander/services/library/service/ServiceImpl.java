package com.alexander.services.library.service;

import com.alexander.services.library.domain.AbstractIdentifiable;
import com.alexander.services.library.dto.AbstractLongIdentifiableDto;
import com.alexander.services.library.dto.AutocompleteData;
import com.alexander.services.library.mapper.BaseMapper;
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
        Page<E> entities = repository.findAll(page == 0 && size == 0 ? Pageable.unpaged() : PageRequest.of(page, size));
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
