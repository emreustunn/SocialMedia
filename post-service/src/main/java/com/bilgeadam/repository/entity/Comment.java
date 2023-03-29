package com.bilgeadam.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment extends BaseEntity{
    @Id
    private String id;
    private String userId;
    private String username;
    private String postId;
    private String content;
    private int like;
    private int disLike;


}
