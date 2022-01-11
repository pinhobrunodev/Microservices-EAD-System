package com.ead.authuser.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDto {

    @NotBlank(message = "Mandatory field.")
    private String username;
    @NotBlank(message = "Mandatory field.")
    private String password;

}
