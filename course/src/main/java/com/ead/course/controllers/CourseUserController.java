package com.ead.course.controllers;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.enums.UserStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseUserService;
import com.ead.course.services.impl.CourseServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private AuthUserClient authUserClient;
    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private CourseUserService courseUserService;

    // CourseDto because is the courses that we need
    @GetMapping(value = "/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "userId",
            direction = Sort.Direction.ASC) Pageable pageable
            , @PathVariable UUID courseId) {
        log.debug("courseId : {}", courseId);
        return ResponseEntity.ok().body(authUserClient.getAllUsersByCourse(courseId, pageable));
    }

    @PostMapping(value = "/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable UUID courseId, @RequestBody @Valid SubscriptionDto dto) {
        ResponseEntity<UserDto> responseUser;
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        if (courseUserService.existsByCourseAndUserId(courseModelOptional.get(), dto.getUserId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists!");
        }
        //CTRL+ALT+T
        try {
            responseUser = authUserClient.getOneUserById(dto.getUserId());
            if (responseUser.getBody().getUserStatus().equals(UserStatus.BLOCKED)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User is blocked.");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        }
        CourseUserModel courseUserModel = courseUserService.save(courseModelOptional.get().convertToCourseUserModel(dto.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
