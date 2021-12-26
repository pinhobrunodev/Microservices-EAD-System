package com.ead.course.controllers;


import com.ead.course.dtos.CourseDto;
import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.impl.CourseServiceImpl;
import com.ead.course.services.impl.ModuleServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/courses/{courseId}/modules")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleServiceImpl moduleService;
    @Autowired
    private CourseServiceImpl courseService;

    @PostMapping
    public ResponseEntity<Object> saveModule(@PathVariable UUID courseId, @Valid @RequestBody ModuleDto moduleDto) {
        Optional<CourseModel> course = courseService.findById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        moduleModel.setCourse(course.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.save(moduleModel));
    }

    @DeleteMapping(value = "/{moduleId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable UUID moduleId, @PathVariable UUID courseId) {
        Optional<ModuleModel> moduleModel = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        moduleService.delete(moduleModel.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");

    }

    @PutMapping(value = "/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable UUID moduleId, @PathVariable UUID courseId, @Valid @RequestBody ModuleDto moduleDto) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (moduleModelOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        var moduleModel = moduleModelOptional.get();
        moduleModel.setTitle(moduleDto.getTitle());
        moduleModel.setDescription(moduleDto.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.save(moduleModel));
    }


    @GetMapping
    public ResponseEntity<List<ModuleModel>> getAllModules(@PathVariable UUID courseId) {
        return ResponseEntity.ok().body(moduleService.findAllByCourse(courseId));
    }

    @GetMapping(value = "/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable UUID moduleId, @PathVariable UUID courseId) {
        Optional<ModuleModel> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        var result = moduleModelOptional.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.") : moduleModelOptional.get();
        return ResponseEntity.ok().body(result);
    }

}
