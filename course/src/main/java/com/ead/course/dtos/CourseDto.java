package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class CourseDto {




    @NotBlank(message = "Mandatory field")
    private String name;
    @NotBlank(message = "Mandatory field")
    private String description;
    private String imageUrl;
    @NotNull(message = "Mandatory field")
    private CourseStatus courseStatus;
    @NotNull(message = "Mandatory field")
    private UUID userInstructor;
    @NotNull(message = "Mandatory field")
    private CourseLevel courseLevel;


}
