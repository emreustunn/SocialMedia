package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank(message = "Username boş geçilemez")
    @Size(min = 3,max = 20,message = "username en az 3 en fazla 20 karakter olmalıdır.")
    private String username;
    @Email
    private String email;
    @Size(min = 3,max = 32,message = "şifre en az 3 en fazla 32 karakter olmalıdır.")
    @NotBlank
    private String password;


}
