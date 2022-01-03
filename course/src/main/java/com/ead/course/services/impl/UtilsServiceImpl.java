package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {


    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {
        return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() +
                "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
    }

    @Override
    public String createUrlGetOneUserById(UUID userId) {
        return "/users/"+userId;
    }

    @Override
    public String createUrlSaveAndSendSubscriptionUserInCourse(UUID userId) {
        return "/users/"+userId+"/courses/subscription";
    }

    @Override
    public String createUrlDeleteCourseInAuthUser(UUID courseId) {
        return "/users/courses/"+courseId;
    }

}
