package com.bilgeadam.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class AuthServiceSecurityConfig {

    @Bean
    JwtTokenFilter getJwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        /**
         * csrfi kapattık
         */
        httpSecurity.csrf().disable();
        /**
         * antMatchers() -> içine verilenlere izinleri verdik yani şifre olmadan girebiliriz.
         * .anyRequest().authenticated() -> geri kalan bütün isteklere şifre sor.
         */
        httpSecurity.authorizeRequests().antMatchers("/v3/api-docs/**","/swagger-ui/**","/api/v1/auth/login","/api/v1/auth/register","/api/v1/auth/register2","/api/v1/auth/activestatus").permitAll().anyRequest().authenticated();
        /**
         * httpSecurity.formLogin(); -> bizi her zorunlu şifre girmemiz gereken yere girdiğimizde
         * springin kendi username ve parola yerine götür.
         */
//        httpSecurity.formLogin();

        /**
         * her istek buraya düşüp burada control edilecek.
         */
        httpSecurity.addFilterBefore(getJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
