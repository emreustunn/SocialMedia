package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateNewPostRequestDto;
import com.bilgeadam.repository.entity.Post;
import com.bilgeadam.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;
@RestController
@RequiredArgsConstructor
@RequestMapping(POST)
public class PostController {
    private final PostService postService;
    @PostMapping(CREATE)
    public ResponseEntity<Post> createPost(@RequestBody CreateNewPostRequestDto dto){
        return ResponseEntity.ok(postService.createPost(dto));
    }


    @GetMapping(FINDALL)
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }
}
