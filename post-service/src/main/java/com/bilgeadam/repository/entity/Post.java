package com.bilgeadam.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Post extends BaseEntity {
    @Id
    private String id;

    private String userId;
    private String username;
    private String title;
    private String content;
    private String mediaUrl;
    private int like;
    private int dislike;


}
