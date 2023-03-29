package com.bilgeadam.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateNewPostRequestDto {
    private String userId;
    private String username;
    private String title;
    private String content;
    private String mediaUrl;
}
