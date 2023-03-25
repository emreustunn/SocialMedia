package com.bilgeadam.dto.request;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginRequestDto {
    private String username;
    private String password;
}
