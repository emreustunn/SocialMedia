package com.bilgeadam.service;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.request.UpdateUserProfileRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.manager.IAuthManager;
import com.bilgeadam.mapper.IUserMapper;
import com.bilgeadam.rabbitmq.model.RegisterModel;
import com.bilgeadam.rabbitmq.procuder.RegisterProducer;
import com.bilgeadam.repository.IUserProfileRepository;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.repository.entity.enums.EStatus;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService extends ServiceManager<UserProfile, String> {
    private final IUserProfileRepository iUserProfileRepository;
    private final JwtTokenManager jwtTokenManager;
    private final IAuthManager iAuthManager;
    private final CacheManager cacheManager;
    private final RegisterProducer registerProducer;

    public UserProfileService(IUserProfileRepository iUserProfileRepository, JwtTokenManager jwtTokenManager, IAuthManager iAuthManager, CacheManager cacheManager, RegisterProducer registerProducer) {
        super(iUserProfileRepository);
        this.iUserProfileRepository = iUserProfileRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.iAuthManager = iAuthManager;
        this.cacheManager = cacheManager;
        this.registerProducer = registerProducer;
    }

    public Boolean createUser(NewCreateUserRequestDto dto) {
        try {
            save(IUserMapper.INSTANCE.toUserProfile(dto));
            return true;
        } catch (Exception exception) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }

    public Boolean createUserWithRabbitMq(RegisterModel model) {
        try {
            UserProfile userProfile = save(IUserMapper.INSTANCE.toUserProfile(model));
            registerProducer.sendNewUser(IUserMapper.INSTANCE.toRegisterElasticModel(userProfile));
            return true;
        } catch (Exception exception) {
            throw new UserManagerException(ErrorType.USER_NOT_CREATED);
        }
    }


    //    public Boolean activateStatus(Long authid) {
//        Optional<UserProfile> userProfile = iUserProfileRepository.findOptionalByAuthId(authid);
    public Boolean activateStatus(String token) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(token);
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = iUserProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        userProfile.get().setStatus(EStatus.ACTIVE);
        update(userProfile.get());
        return true;
    }

    public Boolean update(UpdateUserProfileRequestDto dto) {
        Optional<Long> authId = jwtTokenManager.getIdFromToken(dto.getToken());
        if (authId.isEmpty()) {
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile = iUserProfileRepository.findOptionalByAuthId(authId.get());
        if (userProfile.isEmpty())
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        cacheManager.getCache("findbyusername").evict(userProfile.get().getUsername().toLowerCase());
        if (!dto.getUsername().equals(userProfile.get().getUsername()) || !dto.getEmail().equals(userProfile.get().getEmail())) {
            userProfile.get().setUsername(dto.getUsername());
            UpdateEmailOrUsernameRequestDto updateEmailOrUsernameRequestDto = IUserMapper.INSTANCE.toUpdateEmailOrUsernameRequestDto(dto);
            updateEmailOrUsernameRequestDto.setAuthId(authId.get());
            iAuthManager.updateEmailOrUsername(updateEmailOrUsernameRequestDto);
        }
        userProfile.get().setPhone(dto.getPhone());
        userProfile.get().setAvatar(dto.getAvatar());
        userProfile.get().setAddress(dto.getAddress());
        userProfile.get().setEmail(dto.getEmail());

        userProfile.get().setAbout(dto.getAbout());
        update(userProfile.get());

        return true;
    }

    public Boolean delete(Long id) {
        Optional<UserProfile> auth = iUserProfileRepository.findOptionalByAuthId(id);
        if (auth.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        auth.get().setStatus(EStatus.DELETED);
        update(auth.get());
        return true;
    }

    @Cacheable(value = "findbyusername", key = "#username.toLowerCase()")
    public UserProfile findByUsername(String username) {
        Optional<UserProfile> userProfile = iUserProfileRepository.findOptionalByUsernameIgnoreCase(username);
        if (userProfile.isEmpty()) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return userProfile.get();
    }

    public List<UserProfile> findByRole(String role) {
        List<Long> authIdList = iAuthManager.findByRole(role);
        List<UserProfile> userProfiles = new ArrayList<>();
        authIdList.forEach(x -> {
            userProfiles.add(iUserProfileRepository.findOptionalByAuthId(x).orElse(null)); // bulamazsan null ver.
        });
        return userProfiles;
    }


}
