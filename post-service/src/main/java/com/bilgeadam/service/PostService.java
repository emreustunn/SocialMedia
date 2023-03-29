package com.bilgeadam.service;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.mapper.IPostMapper;
import com.bilgeadam.repository.IPostRepository;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class PostService extends ServiceManager<Post,String> {
    private final IPostRepository postRepository;

    public PostService(IPostRepository postRepository) {
        super(postRepository);
        this.postRepository = postRepository;
    }

    public Post createPost(CreateNewPostRequestDto dto) {
        return save(IPostMapper.INSTANCE.toPost(dto));
    }
}
