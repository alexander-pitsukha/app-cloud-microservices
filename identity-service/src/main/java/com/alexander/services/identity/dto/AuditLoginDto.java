package com.alexander.services.identity.dto;

import com.alexander.services.library.dto.AbstractLongIdentifiableDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuditLoginDto extends AbstractLongIdentifiableDto {

    private UserDto user;

    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("update_date")
    private LocalDateTime updateDate;

}
