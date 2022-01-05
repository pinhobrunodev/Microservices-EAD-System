package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserModelService;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private UserModelService userModelService;

    // CourseDto because is the courses that we need
    @GetMapping(value = "/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(SpecificationTemplate.UserSpec spec, @PageableDefault(page = 0, size = 10, sort = "userId",
            direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable UUID courseId) {
        // Validating if the course have some user... if don't..wont make a sync request
        Optional<CourseModel> course = courseService.findById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        log.debug("courseId : {}", courseId);
        return ResponseEntity.ok().body(userModelService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), pageable));
    }

    // If AuthUser is not working... The Course-MS will continue doing the subscription. Because have a table_users
    @PostMapping(value = "/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId, @RequestBody @Valid SubscriptionDto dto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        // Validating if exists already a USER-COURSE (1-1)
        if (courseService.existsByCourseAndUser(courseId,dto.getUserId())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }
        Optional<UserModel> userModelOptional = userModelService.findById(dto.getUserId());
        // Validating if the User Exists
        if (userModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
        //Validating if the user is ACTIVE OR BLOCKED
        if (userModelOptional.get().getUserStatus().equals(UserStatus.BLOCKED.toString())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
        }
        //Saving on the auxiliary table.
        courseService.saveSubscriptionUserInCourse(courseModelOptional.get().getCourseId(),userModelOptional.get().getUserId());
        log.debug("UserID {} , CourseID {}",userModelOptional.get().getUserId(),courseModelOptional.get().getCourseId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");
    }

}
