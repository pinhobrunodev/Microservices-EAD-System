package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ModuleDto {

    @NotBlank(message = "Mandatory field")
    private String title;
    @NotBlank(message = "Mandatory field")
    private String description;

}
