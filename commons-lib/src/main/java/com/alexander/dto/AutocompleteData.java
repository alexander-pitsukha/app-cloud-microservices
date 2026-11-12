package com.alexander.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutocompleteData extends AbstractLongIdentifiableDto {

    private String label;

    public AutocompleteData(Long id, String label) {
        setId(id);
        setLabel(label);
    }

}
