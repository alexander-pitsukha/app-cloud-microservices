package com.alexander.services.library.dto;

import com.alexander.services.library.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractLongIdentifiableDto implements Identifiable<Long> {

    private Long id;

}
