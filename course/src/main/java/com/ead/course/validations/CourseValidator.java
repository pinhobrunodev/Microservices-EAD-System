package com.ead.course.validations;

import com.ead.course.dtos.CourseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    private Validator validator;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDto courseDto = (CourseDto) target;
        // Same as @Valid -> Validating each attribute..
        validator.validate(courseDto, errors);
        // If is Ok. validate the Instructor
        if (!errors.hasErrors()) {
            validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }


    private void validateUserInstructor(UUID userInstructor, Errors errors) {
//        ResponseEntity<UserDto> responseUserInstructor;
//        try {
//            responseUserInstructor = authUserClient.getOneUserById(userInstructor);
//            if (responseUserInstructor.getBody().getUserType().equals(UserType.STUDENT)) {
//                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN.");
//            }
//        } catch (HttpStatusCodeException e) {
//            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
//                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
//            }
//        }
    }
}
