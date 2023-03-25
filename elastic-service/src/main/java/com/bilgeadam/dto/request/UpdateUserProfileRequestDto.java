package com.bilgeadam.dto.request;

import lombok.*;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserProfileRequestDto {
    private String token;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;
}
