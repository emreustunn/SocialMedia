package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ActivitaionRequestDto;
import com.bilgeadam.dto.request.LoginRequestDto;
import com.bilgeadam.dto.request.RegisterRequestDto;
import com.bilgeadam.dto.request.UpdateEmailOrUsernameRequestDto;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.enums.ERoles;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;
    private final JwtTokenManager jwtTokenManager;
    private final CacheManager cacheManager;

    @PostMapping(REGISTER)
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(REGISTER+"2")
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto) {
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @PutMapping(ACTIVESTATUS)
    public ResponseEntity<Boolean> activationCheck(ActivitaionRequestDto dto) {
        return ResponseEntity.ok(authService.activationCheck(dto));
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(FINDALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping("/createtoken")
    public ResponseEntity<String> createToken(Long id, ERoles role) {
        return ResponseEntity.ok(jwtTokenManager.createToken(id, role).get());
    }

    @GetMapping("/createtoken2")
    public ResponseEntity<String> createToken(Long id) {
        return ResponseEntity.ok(jwtTokenManager.createToken(id).get());
    }

    @GetMapping("/getidfromtoken")
    public ResponseEntity<Long> getIdFromToken(String token) {
        return ResponseEntity.ok(jwtTokenManager.getIdFromToken(token).get());
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/getrolefromtoken")
    public ResponseEntity<String> getRoleFromToken(String token) {
        return ResponseEntity.ok(jwtTokenManager.getRoleFromToken(token).get());
    }

    @PutMapping("/updateemailorusername")
    public ResponseEntity<Boolean> updateEmailOrUsername(@RequestBody UpdateEmailOrUsernameRequestDto dto) {
        return ResponseEntity.ok(authService.updateEmailOrUsername(dto));
    }

    //delete işleminde verimizi silmiyoruz sadece statusu değiştiriyoruz.
    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long id) {
        return ResponseEntity.ok(authService.delete(id));
    }

    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value) {
        try {
            Thread.sleep(2000);
            return value;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    //1. silme işlemi
    @GetMapping("/redisdelete")
    @CacheEvict(cacheNames = "redisexample", allEntries = true)
    public void redisDelete() {
    }

    //2. silme işlemi
    @GetMapping("/redisdelete2")
    public boolean redisDelete2() {
        try {
//            cacheManager.getCache("redisexample").clear(); // aynı isimle cachelenmiş bütün verileri siler.
            cacheManager.getCache("redisexample").evict("emre"); // redisexampledaki emre değerlerini sil.
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @GetMapping("/findbyrole")
    public List<Long> findByRole(@RequestParam String role) {
        return authService.findByRole(role);
    }

}
