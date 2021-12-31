package com.ead.course.dtos;


import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class SubscriptionDto {

    @NotNull(message = "Cannot sent null values")
    public UUID userId;


}
