package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {


    private String url;

    @Autowired
    private UtilsService utilsService;
    @Autowired
    private RestTemplate restTemplate;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;


    public Page<UserDto> getAllUsersByCourse(UUID userId, Pageable pageable) {
        List<UserDto> searchResult = null;
        url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(userId, pageable);
        log.debug("Request URL : {}", url);
        log.info("Request URL : {}", url);
        try {
            // Allows the Pagination from ResponsePageDto of UserDto
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {
            };
            ResponseEntity<ResponsePageDto<UserDto>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements : {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request /courses {}", e);
        }
        log.info("Ending request /users courseId {} ", userId);
        return new PageImpl<>(searchResult);
    }

    public ResponseEntity<UserDto> getOneUserById(UUID userId) {
        url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetOneUserById(userId);
        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }


    public void postSubscriptionUserInCourse(UUID userId, UUID courseId) {
        url = REQUEST_URL_AUTHUSER + utilsService.createUrlSaveAndSendSubscriptionUserInCourse(userId);
        var courseUserDto = new CourseUserDto();
        courseUserDto.setUserId(userId);
        courseUserDto.setCourseId(courseId);
        restTemplate.postForObject(url, courseUserDto, String.class);
    }

    public void deleteCourseInAuthUser(UUID courseId) {
        url = REQUEST_URL_AUTHUSER + utilsService.createUrlDeleteCourseInAuthUser(courseId);
        restTemplate.exchange(url,HttpMethod.DELETE,null,String.class);
    }
}
