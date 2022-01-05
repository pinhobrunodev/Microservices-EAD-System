package com.ead.authuser.dtos;

import lombok.Data;

import java.util.UUID;
// Dto to send the message to the Broker
@Data
public class UserEventDto {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private String actionType; // Type of action -> CREATE,UPDATE,DELETE


}
