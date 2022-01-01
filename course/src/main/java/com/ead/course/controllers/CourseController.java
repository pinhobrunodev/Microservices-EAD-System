package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validations.CourseValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/courses")
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;
    @Autowired
    private CourseValidator courseValidator;


    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto courseDto, Errors errors) {
        // Validating the fields of courseDto and validating the type of user making a SyncCall do AuthUser-MS
        courseValidator.validate(courseDto, errors);
        if(errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        var course = new CourseModel();
        BeanUtils.copyProperties(courseDto, course);
        course.setCreationDate(LocalDateTime.now());
        course.setLastUpdateDate(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(course));
    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID courseId) {
        Optional<CourseModel> course = courseService.findById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        courseService.delete(course.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");

    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable UUID courseId, @Valid @RequestBody CourseDto courseDto) {
        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (courseModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        var courseModel = courseModelOptional.get();
        courseModel.setName(courseDto.getName());
        courseModel.setDescription(courseDto.getDescription());
        courseModel.setImageUrl(courseDto.getImageUrl());
        courseModel.setCourseStatus(courseDto.getCourseStatus());
        courseModel.setCourseLevel(courseDto.getCourseLevel());
        courseModel.setLastUpdateDate(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }


    @GetMapping
    public ResponseEntity<Page<CourseModel>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                           @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC)
                                                                   Pageable pageable, @RequestParam(required = false) UUID userId) {
        Page<CourseModel> userModelPage = null; // Two ways to enter the pagination
        if (userId != null) {
            userModelPage = courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        } else {
            userModelPage = courseService.findAll(spec, pageable);
        }
        return ResponseEntity.ok().body(userModelPage);
    }


    @GetMapping(value = "/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable UUID courseId) {
        Optional<CourseModel> course = courseService.findById(courseId);
        var result = course.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.") : course.get();
        return ResponseEntity.ok().body(result);
    }

}
