package com.ead.notification.services.impl;

import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.repositories.NotificationModelRepository;
import com.ead.notification.services.NotificationModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Page<NotificationModel> findAllNotificationByUser(UUID userId, Pageable pageable) {
        return notificationModelRepository.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageable);
    }

    @Override
    public Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationModelRepository.findByNotificationIdAndUserId(notificationId,userId);
    }
}
