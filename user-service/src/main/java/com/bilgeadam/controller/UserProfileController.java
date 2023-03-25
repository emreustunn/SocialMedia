package com.bilgeadam.controller;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UpdateUserProfileRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(USER)
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @PutMapping(ACTIVESTATUS+"/{authid}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authid){
        return ResponseEntity.ok(userProfileService.activateStatus(authid));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> update(@RequestBody UpdateUserProfileRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long id) {
        return ResponseEntity.ok(userProfileService.delete(id));
    }

    @GetMapping(FINDALL)
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping("/findbyusername")
    public ResponseEntity<UserProfile> findByUsername(@RequestParam String username){
        return ResponseEntity.ok(userProfileService.findByUsername(username));
    }

    @GetMapping("/findbyrole")
    public ResponseEntity<List<UserProfile>> findByRole(@RequestParam String role){
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }
}
