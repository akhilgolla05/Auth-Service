package org.example.eventProducer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Builder
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoEvent {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
