package com.bilgeadam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailOrUsernameRequestDto {
    private Long authId;
    private String email;
    private String username;
}
