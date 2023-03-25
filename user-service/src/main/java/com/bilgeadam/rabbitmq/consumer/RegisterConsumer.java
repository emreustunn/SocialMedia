package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j //console'a log info çıktısı vermek için kullanılan kütüphane.
public class RegisterConsumer {

    private final UserProfileService userProfileService;


    @RabbitListener(queues = ("${rabbitmq.queueRegister}"))
    public void newUserCreate(RegisterModel model){
        log.info("User {}",model.toString());
        userProfileService.createUserWithRabbitMq(model);
//        userProfileService.createUser(IUserMapper.INSTANCE.toNewCreateUserRequestDto(model)); //2.tercih
    }

}
