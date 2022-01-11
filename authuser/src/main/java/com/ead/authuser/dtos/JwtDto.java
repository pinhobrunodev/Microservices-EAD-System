package com.ead.authuser.dtos;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor // To build the Jwt only with obligatory attributes ( token )
public class JwtDto {


    @NonNull // -> Specifying the only parameter on the constructor
    private String token;
    private String type = "Bearer";

}
