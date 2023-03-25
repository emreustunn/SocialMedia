package com.bilgeadam.manager;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.repository.entity.Auth;
import org.mapstruct.Mapping;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.bilgeadam.constants.ApiUrls.*;


@FeignClient(url = "http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")
public interface IUserManager {
    @PostMapping("/create")
    ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto);

    @GetMapping(ACTIVESTATUS+"/{authid}")
    ResponseEntity<Boolean> activateStatus(@PathVariable Long authid);

    @DeleteMapping(DELETEBYID)
    ResponseEntity<Boolean> delete(@RequestParam Long id);



}
