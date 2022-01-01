package com.ead.authuser.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class InstructorDto {

    @NotNull(message = "Cannot be null.")
    private UUID userId;
}
