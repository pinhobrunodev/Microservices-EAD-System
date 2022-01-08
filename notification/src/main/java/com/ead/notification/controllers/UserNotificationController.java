package com.ead.notification.controllers;

import com.ead.notification.dtos.NotificationDto;
import com.ead.notification.models.NotificationModel;
import com.ead.notification.services.NotificationModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserNotificationController {

    final NotificationModelService notificationModelService;

    public UserNotificationController(NotificationModelService notificationModelService) {
        this.notificationModelService = notificationModelService;
    }

    @GetMapping(value = "/users/{userId}/notifications")
    public ResponseEntity<Page<NotificationModel>> getAllNotificationsByUser(@PathVariable UUID userId,
                                                                             @PageableDefault(page = 0, size = 10, sort = "notificationId", direction = Sort.Direction.ASC)
                                                                                     Pageable pageable) {
    return ResponseEntity.ok().body(notificationModelService.findAllNotificationByUser(userId,pageable));
    }

    @PutMapping(value = "/users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> updateNotification(@PathVariable UUID userId,
                                                     @PathVariable UUID notificationId,
                                                     @RequestBody @Valid NotificationDto notificationDto){
        Optional<NotificationModel> notificationModelOptional = notificationModelService.findByNotificationIdAndUserId(notificationId,userId);
        if(notificationModelOptional.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Notification not found!");
        }
        notificationModelOptional.get().setNotificationStatus(notificationDto.getNotificationStatus());
        notificationModelService.saveNotification(notificationModelOptional.get());
        return ResponseEntity.ok().body(notificationModelOptional.get());
    }
}
