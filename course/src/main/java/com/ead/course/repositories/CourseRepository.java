package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    /**
     * @param courseId
     * @param userId
     * @return if found a user with a course = courseId and userId that were sent... so the count will increase and return true... if not return false.
     */
    @Query(value = "select case when count(tcu) > 0  THEN true ELSE false END FROM tb_courses_users tcu WHERE tcu.course_id= :courseId and tcu.user_Id = :userId", nativeQuery = true)
    boolean existsByCourseAndUser(UUID courseId, UUID userId);

    @Modifying
    @Query(value = "insert into tb_courses_users values (:courseId,:userId);", nativeQuery = true)
    void saveCourseUser(UUID courseId, UUID userId);

    @Modifying
    @Query(value = "delete from tb_courses_users where course_id = :courseId", nativeQuery = true)
    void deleteCourseUserByCourse(UUID courseId);

    @Modifying
    @Query(value = "delete from tb_courses_users where user_id = :userId", nativeQuery = true)
    void deleteCourseUserByUser(UUID userId);


}
