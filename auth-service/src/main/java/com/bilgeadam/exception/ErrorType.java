package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {
    INTERNAL_ERROR(5100,"Sunucu hatasi",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4100,"Parametre hatasi",HttpStatus.BAD_REQUEST),
    LOGIN_ERROR(4110,"Kullanici adi veya şifre hatali",HttpStatus.BAD_REQUEST),
    USERNAME_DUPLICATE(4111,"Kullanici adi daha önce alınmış",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4112,"Böyle bir kullanıcı bulunamadı",HttpStatus.BAD_REQUEST),
    ACTIVATE_CODE_ERROR(4113,"Aktivasyon kodu eşleşmiyor",HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4114,"Geçersiz token",HttpStatus.BAD_REQUEST),
    TOKEN_NOT_CREATED(4116,"Token oluşturulamadı!",HttpStatus.BAD_REQUEST),
    NOT_ACTIVE_ACCOUNT(4115,"Lütfen önce hesabınızı aktif hale getiriniz!",HttpStatus.FORBIDDEN),
    USER_NOT_CREATED(3116,"User oluşturulamadı..." , HttpStatus.BAD_REQUEST)

    ;
    private int code;
    private String message;
    HttpStatus httpStatus;


}
