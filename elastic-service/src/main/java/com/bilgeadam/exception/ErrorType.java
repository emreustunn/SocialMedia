package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(5200, "Sunucu hatasi", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4200, "Parametre hatasi", HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4210, "Kullanici adi daha önce alınmış", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4211, "Böyle bir kullanıcı bulunamadı", HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(4212,"Kullanıcı kayıt edilemedi!",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4213,"Geçersiz token",HttpStatus.BAD_REQUEST),
    ;
    private int code;
    private String message;
    HttpStatus httpStatus;


}
