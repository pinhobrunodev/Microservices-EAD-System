package com.ead.notification.services.impl;

import com.ead.notification.models.NotificationModel;
import com.ead.notification.repositories.NotificationModelRepository;
import com.ead.notification.services.NotificationModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationModelServiceImpl implements NotificationModelService {

     final NotificationModelRepository notificationModelRepository;

    public NotificationModelServiceImpl(NotificationModelRepository notificationModelRepository) {
        this.notificationModelRepository = notificationModelRepository;
    }

    @Override
    public NotificationModel saveNotification(NotificationModel notificationModel) {
        return notificationModelRepository.save(notificationModel);
    }
}
