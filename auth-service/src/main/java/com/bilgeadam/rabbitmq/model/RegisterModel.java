package com.bilgeadam.rabbitmq.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterModel implements Serializable {
    private Long authId;
    private String username;
    private String email;
}
