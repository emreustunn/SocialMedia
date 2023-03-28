package com.bilgeadam.manager;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
;import static com.bilgeadam.constants.ApiUrls.ACTIVATESTATUS;
import static com.bilgeadam.constants.ApiUrls.DELETEBYID;


@FeignClient(url = "http://localhost:7072/api/v1/user",decode404 = true,name = "auth-userprofile")
public interface IUserManager {

    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto);

    @DeleteMapping(DELETEBYID)
    public ResponseEntity<Boolean> delete(@RequestParam Long authId);


    @PostMapping(ACTIVATESTATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestHeader(value = "Authorization") String token);
}
