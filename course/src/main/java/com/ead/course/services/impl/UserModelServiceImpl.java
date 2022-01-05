package com.ead.course.services.impl;

import com.ead.course.models.UserModel;
import com.ead.course.repositories.UserModelRepository;
import com.ead.course.services.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserModelServiceImpl implements UserModelService {

    @Autowired
    private UserModelRepository userModelRepository;

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable pageable) {
        return userModelRepository.findAll(spec,pageable);
    }
    @Override
    public Optional<UserModel> findById(UUID userInstructor) {
        return userModelRepository.findById(userInstructor);
    }

    @Override
    public UserModel saveUserEventState(UserModel userModel) {
        return userModelRepository.save(userModel);
    }

    @Override
    public void deleteUserEventState(UUID userId) {
        userModelRepository.deleteById(userId);
    }


}
