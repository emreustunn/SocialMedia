package com.bilgeadam.service;

import com.bilgeadam.repository.ILikeRepository;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Like;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class LikeService extends ServiceManager<Like,String> {
    private final ILikeRepository likeRepository;

    public LikeService(ILikeRepository likeRepository) {
        super(likeRepository);
        this.likeRepository = likeRepository;
    }
}
