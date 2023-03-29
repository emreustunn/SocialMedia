package com.bilgeadam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
@AllArgsConstructor
public enum ErrorType {

    INTERNAL_ERROR(5300,"Sunucu Hatası",HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST(4300,"Parametre Hatası",HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(4310,"Böyle bir kullanıcı bulunamadı",HttpStatus.NOT_FOUND),
    INVALID_TOKEN(4311, "Geçersiz Token",HttpStatus.BAD_REQUEST),

    ;
    private int code;
    private String message;
    HttpStatus httpStatus;


}
