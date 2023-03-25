package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UpdateUserProfileRequestDto;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.procuder.RegisterProducer;
import com.bilgeadam.repository.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,componentModel = "spring")
public interface IUserMapper {
    IUserMapper INSTANCE= Mappers.getMapper(IUserMapper.class);

    UserProfile toUserProfile(final NewCreateUserRequestDto dto);
    UpdateEmailOrUsernameRequestDto toUpdateEmailOrUsernameRequestDto(final UpdateUserProfileRequestDto dto);

    UserProfile toUserProfile(final RegisterModel model);

    NewCreateUserRequestDto toNewCreateUserRequestDto(final RegisterModel model);

    RegisterElasticModel toRegisterElasticModel(final UserProfile userProfile);
}
