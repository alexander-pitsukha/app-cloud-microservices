package com.alexander.services.library.mapper;

import com.alexander.services.library.domain.AbstractIdentifiable;
import com.alexander.services.library.dto.AbstractLongIdentifiableDto;

public interface BaseMapper<E extends AbstractIdentifiable<Long>, D extends AbstractLongIdentifiableDto> {

    D toDto(E entity);

    E toEntity(D dto);

}
