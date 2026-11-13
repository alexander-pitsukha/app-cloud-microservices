package com.alexander.mapper;

import com.alexander.domain.AbstractIdentifiable;
import com.alexander.dto.AbstractLongIdentifiableDto;

public interface BaseMapper<E extends AbstractIdentifiable<Long>, D extends AbstractLongIdentifiableDto> {

    D toDto(E entity);

    E toEntity(D dto);

}
