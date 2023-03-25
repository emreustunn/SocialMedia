package com.bilgeadam.rabbitmq.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterMailModel implements Serializable {
    private String email;
    private String username;
    private String activationCode;
}
