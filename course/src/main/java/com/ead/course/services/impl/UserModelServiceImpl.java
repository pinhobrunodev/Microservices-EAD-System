package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.repositories.UserModelRepository;
import com.ead.course.services.UserModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserModelServiceImpl implements UserModelService {

    @Autowired
    private UserModelRepository userModelRepository;

}
