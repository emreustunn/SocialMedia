package com.bilgeadam.repository.entity;

import com.bilgeadam.repository.entity.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "user_profile")
public class UserProfile extends BaseEntity {
    @Id
    private String id;
    private Long authId;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String about;
    @Builder.Default
    private EStatus status = EStatus.PENDING;

}
