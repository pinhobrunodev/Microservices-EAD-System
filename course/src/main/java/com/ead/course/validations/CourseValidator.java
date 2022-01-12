package com.ead.course.validations;

import com.ead.course.configs.security.AuthenticationCurrentUserService;
import com.ead.course.configs.security.UserDetailsImpl;
import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserModelRepository;
import com.ead.course.services.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    private Validator validator;
    @Autowired
    private UserModelService userModelService;
    @Autowired
    private AuthenticationCurrentUserService authenticationCurrentUserService;


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

    // Validating when insert Course.
    private void validateUserInstructor(UUID userInstructor, Errors errors) {
        UUID currentUserId = authenticationCurrentUserService.getCurrentUser().getUserId();
        if (currentUserId.equals(userInstructor)) {
            Optional<UserModel> userModelOptional = userModelService.findById(userInstructor);
            if (userModelOptional.isEmpty()) {
                errors.rejectValue("userInstructor", "UserInstructorError", "Instructor not found.");
            }
            if (userModelOptional.get().getUserType().equals(UserType.STUDENT.toString())) {
                errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN!");
            }
        }
        else {
            throw  new AccessDeniedException("Forbidden");
        }

    }
}
