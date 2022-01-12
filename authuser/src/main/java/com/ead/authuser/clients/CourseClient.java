package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UtilsService utilsService;
    @Value("${ead.api.url.course}")
    String REQUEST_URL_COURSE;

    private String url;


    // Setting the Retry Resilience and FallbackMethod when retry 3 times to send the request
    // Retry is not a good one because we are sending requests to a Microservice that already is having problems... so Circuit Breaker is BETTER
    //@Retry(name = "retryInstance", fallbackMethod = "retryFallback")

    //  Don't make sense send an empty pagination ... Client will think that he don't have any subscription in a course
    @CircuitBreaker(name = "circuitbreakerInstance")
    // fallback -> send to an Error QUEUE to another MS listen and treat
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {
        List<CourseDto> searchResult = null;
        url = REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUser(userId, pageable);
        HttpHeaders httpHeaders = new HttpHeaders(); // Create the instance of the Header
        httpHeaders.set("Authorization", token); // Setting the token on parameter 'Authorization' that came from the request.
        HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", httpHeaders); // Creating the RequestEntity with the token on Authorization parameter
        log.debug("Request URL : {}", url);
        log.info("Request URL : {}", url);
        // Allows the Pagination from ResponsePageDto OF CourseDto
        ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {
        };                                                                                              // Now  we are throwing a header with the authorization that came on the request
        ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
        searchResult = result.getBody().getContent();
        log.debug("Response Number of Elements : {}", searchResult.size());
        log.info("Ending request /courses userId {} ", userId);
        return new PageImpl<>(searchResult);
    }

    /*
    // Need the same RETURN and the same PARAMETERS of the Original Method  and  Exception Parameter (Throwable -> Superclass of Exceptions Java Throw)
    public Page<CourseDto> retryFallback(UUID userId, Pageable pageable, Throwable t) {
        log.error("Inside retry retryFallback, cause - {}", t.toString());
        // Empty pagination
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult);
    }
*/
}
