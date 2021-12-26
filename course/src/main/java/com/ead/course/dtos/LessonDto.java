package com.ead.course.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LessonDto {

    @NotBlank(message = "Mandatory field")
    private String title;
    @NotBlank(message = "Mandatory field")
    private String description;
    @NotBlank(message = "Mandatory field")
    private String videoUrl;
}
