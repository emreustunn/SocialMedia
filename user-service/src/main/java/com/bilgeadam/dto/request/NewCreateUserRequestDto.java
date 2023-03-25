package com.bilgeadam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCreateUserRequestDto {
    private Long authId;
    private String username;
    private String email;
}
