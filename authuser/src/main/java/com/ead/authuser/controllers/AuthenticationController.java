package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.services.impl.UserServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Log4j2 // Lombok generate automatic the Log , don't need to declare in all classes the method.
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth")
public class AuthenticationController {


    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/signup")
    public ResponseEntity<Object> registerUser(@RequestBody
                                               @Validated(UserDto.UserView.RegistrationPost.class)
                                               @JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto) {
        log.debug("POST registerUser userDto received {}",userDto.toString());

        if (userService.existsByUsername(userDto.getUsername())) {
            // We can categorize logs for security ( Ex: User sent the same username in to many requests.. we need to send an Warn for the application )
            log.warn("Username {} is Already Taken! ",userDto.getUsername());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            log.warn("Email {} is Already Taken! ",userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(LocalDateTime.now());
        userModel.setLastUpdateDate(LocalDateTime.now());
        userService.saveUserAndPublishEvent(userModel);
        log.debug("POST registerUser userModel saved {}",userModel.toString());
        log.info("User saved successfully userId {}",userModel.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @GetMapping("/")
    public String index(){
        log.trace("TRACE"); // -> Used to visualize with more  details
        log.debug("DEBUG"); // -> Used to development environment when people are developing.... Ex : Values of some variable....
        log.info("INFO"); // -> Used to visualize important infos not so detailed like the "trace"... Very common on Production....Only RELEVANT information
        log.warn("WARN"); // -> Used to visualize conflicts... show a alert... but is not a error..
        log.error("ERROR"); // -> Used when something go wrong... Very used on TryCatch... on the block Catch...to see what error is launched.
       /* try {
            throw  new Exception("Exception message");
        }catch (Exception e){
            log.error("------ERROR--------",e);
        }*/
        return "Logging Spring Boot...";
    }


}
