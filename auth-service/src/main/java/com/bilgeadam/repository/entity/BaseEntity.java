package com.bilgeadam.repository.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
    private Long createDate;
    private Long updateDate;

}
