package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UserModelRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {
    Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable);
}
