package com.bilgeadam.service;


import com.bilgeadam.mapper.IElasticMapper;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository userProfileRepository;


    public UserProfileService(IUserProfileRepository userProfileRepository) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;

    }


    public UserProfile createUserWithRabbitMq(RegisterElasticModel model) {
        return  save(IElasticMapper.INSTANCE.toUserProfile(model));
    }
}
