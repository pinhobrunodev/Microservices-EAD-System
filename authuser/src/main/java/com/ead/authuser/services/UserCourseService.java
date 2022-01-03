package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(UserModel userModel, UUID courseId);

    UserCourseModel save(UserCourseModel userCourseModel);
    // Validate if the UUID of a Course Exists on Table UserCourse
    boolean existsByCourseId(UUID courseId);

    void deleteUserCourseByCourse(UUID courseId);
}
