package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CourseUserRepository extends JpaRepository<CourseUserModel, UUID> {
    boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId);

    @Query(value = "select * from tb_courses_users where course_id = :courseId",nativeQuery = true)
    List<CourseUserModel> findAllCourseUserIntoCourse(UUID courseId);

    boolean existsByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);
}
