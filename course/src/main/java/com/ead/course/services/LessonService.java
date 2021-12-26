package com.ead.course.services;

import com.ead.course.models.LessonModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {
    LessonModel save(LessonModel lessonModel);

    void delete(LessonModel lessonModel);


    Optional<LessonModel> findLessonIntoModule(UUID moduleId, UUID lessonId);


    List<LessonModel> findAllByModule(UUID moduleId);
}
