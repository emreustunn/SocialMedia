package com.bilgeadam.controller;

import com.bilgeadam.dto.request.CreateFollowRequestDto;
import com.bilgeadam.repository.entity.Follow;
import com.bilgeadam.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(FOLLOW)
public class FollowController {

    private final FollowService followService;

    @PostMapping(CREATE)
    public ResponseEntity<?> createFollow(@RequestBody CreateFollowRequestDto dto) {
        return ResponseEntity.ok(followService.createFollow(dto));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<Follow>> findAll() {
        return ResponseEntity.ok(followService.findAll());
    }


}
