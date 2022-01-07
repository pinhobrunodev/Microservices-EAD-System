package com.ead.authuser.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope // -> Always when update a specific value will restart the context of the application with the new properties.
public class RefreshScopeController {

    @Value("${authuser.refreshscope.name}")
    private String name;

    // Very common to update configurations while running ( ONLY BASIC CONFIGURATIONS - RECOMMENDED )
    @RequestMapping(value = "/refreshscope")
    String refreshscope(){
        return name;
    }

}
