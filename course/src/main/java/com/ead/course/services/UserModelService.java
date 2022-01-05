package com.ead.course.services;

import com.ead.course.models.UserModel;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface UserModelService {
    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);

    UserModel saveUserEventState(UserModel userModel);
}
