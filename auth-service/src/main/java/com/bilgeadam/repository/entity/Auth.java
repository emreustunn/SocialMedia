package com.bilgeadam.repository.entity;

import com.bilgeadam.repository.entity.enums.ERoles;
import com.bilgeadam.repository.entity.enums.EStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Auth extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String activationCode;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ERoles role=ERoles.USER;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EStatus status=EStatus.PENDING;
}
