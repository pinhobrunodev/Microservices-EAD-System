package com.ead.notification.repositories;

import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.NotificationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface NotificationModelRepository extends JpaRepository<NotificationModel, UUID> {

    Page<NotificationModel> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);

    Optional<NotificationModel> findByNotificationIdAndUserId(UUID notificationId, UUID userId);
}
