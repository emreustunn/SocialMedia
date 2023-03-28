package com.bilgeadam.service;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.producer.RegisterProducer;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    user microservisinde findbyrole diye bir metot yazalım bu metot girdiğimiz role gore
    bize databasedeki userprofile ları donsun ayrıcıca bu methodu cachleyelim
    birde bu cache ne zman ve nasıl silinir bunla ilgili kod eklemelerinide yapalım
 */
@Service
public class UserProfileService extends ServiceManager<UserProfile,String> {

    private final IUserProfileRepository userProfileRepository;

    private final JwtTokenManager jwtTokenManager;

    private final IAuthManager authManager;

    private final CacheManager cacheManager;

    private final RegisterProducer registerProducer;
    public UserProfileService(IUserProfileRepository userProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager authManager, CacheManager cacheManager, RegisterProducer producer, RegisterProducer registerProducer) {
        super(userProfileRepository);
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.authManager = authManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
    }

    public Boolean createUser(NewCreateUserRequestDto dto) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return  true;
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            UserProfile userProfile= save(IUserMapper.INSTANCE.toUserProfile(model));
            //rabbitmq ile elastic-service veri gonderelicek
            registerProducer.sendNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(userProfile));
            return  true;
        }catch (Exception e){
            throw  new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }
    public Boolean activateStatus(String token) {
        Optional<Long> authId=jwtTokenManager.getIdFromToken(token.substring(7));
        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }



    public Boolean update(UserProfileUpdateRequestDto dto){
        Optional<Long> authId=jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername().toLowerCase());
        if ( !dto.getUsername().equals(userProfile.get().getUsername())||!dto.getEmail().equals(userProfile.get().getEmail())){
            userProfile.get().setUsername(dto.getUsername());
            userProfile.get().setEmail(dto.getEmail());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto=IUserMapper.INSTANCE.toUpdateEmailOrUsernameRequestDto(dto);
            updateEmailOrUsernameRequestDto.setAuthId(authId.get());
            authManager.updateEmailOrUsername("Bearer "+dto.getToken(),updateEmailOrUsernameRequestDto);
        }

        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());

        return  true;
    }

    public Boolean delete(Long authId) {
        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByAuthId(authId);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.DELETED);
        update(userProfile.get());
        return  true;
    }

    @Cacheable(value = "findbyusername",key ="#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Optional<UserProfile> userProfile=userProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()){
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    @Cacheable(value = "findbyrole",key ="#role.toUpperCase()")
    public List<UserProfile> findByRole(String role,String token) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //ResponseEntity<List<Long>>authIds2=authManager.findByRole(role);
        List<Long> authIds=authManager.findByRole(token,role).getBody();

        return authIds.stream().map(x-> userProfileRepository.findOptionalByAuthId(x)
                        .orElseThrow(()->{throw new UserManagerException(ErrorType.USER_NOT_FOUND);}))
                .collect(Collectors.toList());
    }

    public Optional<UserProfile> findByAuthId(Long authId) {
        return userProfileRepository.findOptionalByAuthId(authId);
    }
}
