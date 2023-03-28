package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateFollowRequestDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserManagerException;
import com.bilgeadam.mapper.IFollowMapper;
import com.bilgeadam.repository.IFollowRepository;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.utility.JwtTokenManager;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class FollowService extends ServiceManager {

    private final IFollowRepository followRepository;

    private final JwtTokenManager jwtTokenManager;

    private final UserProfileService userProfileService;

    public FollowService(IFollowRepository followRepository, JwtTokenManager jwtTokenManager, UserProfileService userProfileService) {
        super(followRepository);
        this.followRepository = followRepository;
        this.jwtTokenManager = jwtTokenManager;
        this.userProfileService = userProfileService;
    }
    @Transactional
    public Boolean createFollow(CreateFollowRequestDto dto) {
        Follow follow;
        Optional<Long> authId=jwtTokenManager.getIdFromToken(dto.getToken());

        if (authId.isEmpty()){
            throw new UserManagerException(ErrorType.INVALID_TOKEN);
        }
        Optional<UserProfile> userProfile=userProfileService.findByAuthId(authId.get());
        Optional<UserProfile> followUser=userProfileService.findById(dto.getFollowId());

        Optional<Follow> followDb=followRepository.
                findByUserIdAndFollowId(userProfile.get().getId(),followUser.get().getId());

        if (followDb.isPresent()){
            throw new UserManagerException(ErrorType.FOLLOW_ALREADY_EXIST);
        }

        if (userProfile.isPresent()&&followUser.isPresent()){
            follow=IFollowMapper.INSTANCE.toFollow(dto,userProfile.get().getId());
            follow= (Follow) save(follow);
            userProfile.get().getFollows().add(followUser.get().getId());
            followUser.get().getFollower().add(userProfile.get().getId());
            userProfileService.update(userProfile.get());
            userProfileService.update(followUser.get());
        }else{
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }
        return  true;
    }
}
