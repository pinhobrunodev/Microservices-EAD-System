package com.ead.course.services.impl;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publishers.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.repositories.UserModelRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserModelService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private NotificationCommandPublisher notificationCommandPublisher;

    // Deleting in Cascade
    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if (!modules.isEmpty()) {
            for (ModuleModel module : modules) {
                List<LessonModel> lessons = lessonRepository.findAllLessonsIntoModule(module.getModuleId());
                if (!lessons.isEmpty()) {
                    lessonRepository.deleteAll(lessons);
                }
            }
            moduleRepository.deleteAll(modules);
        }
        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return courseRepository.existsByCourseAndUser(courseId, userId);
    }

   /* @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courseRepository.saveCourseUser(courseId, userId);
    }*/

    // Command event -> Notification
    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendPublishCommandEventNotification(CourseModel courseModel,UserModel userModel){
        courseRepository.saveCourseUser(courseModel.getCourseId(),userModel.getUserId());
        try { // if have some error... not in the exact moment the student will receive the notification but with try catch without throw new , will occur the sending
            var notificationCommandDto = new NotificationCommandDto();
            notificationCommandDto.setTitle("Bem-Vindo(a) ao Curso: "+courseModel.getName());
            notificationCommandDto.setMessage(userModel.getFullName()+ " a sua inscrição foi realizada com sucesso!");
            notificationCommandDto.setUserId(userModel.getUserId());
            notificationCommandPublisher.publishUserEvent(notificationCommandDto);
        }catch (Exception e){
            log.error("Error sending notification"); // Allows the continuous flow and don't stop the application.
        }
    }


}
