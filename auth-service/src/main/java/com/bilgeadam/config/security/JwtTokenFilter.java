package com.bilgeadam.config.security;

import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.utility.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenManager jwtTokenManager;
    @Autowired
    private JwtUserDetails jwtUserDetails;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader= request.getHeader("Authorization");
        System.out.println("---> " + authorizationHeader);

        filterChain.doFilter(request,response);
        /**
         * SecurityContextHolder.getContext().getAuthentication() -> daha önce authontike olmamışsa
         */
        if(authorizationHeader!=null  && authorizationHeader.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication()==null){
            /**
             * authorizationHeader.substring(7); -> tokenı Bearer kısmından ayırdık.
             */
            String token=authorizationHeader.substring(7);
            Optional<Long> id = jwtTokenManager.getIdFromToken(token);
            if(id.isPresent()){
                UserDetails userDetails = jwtUserDetails.loadUserByUserId(id.get());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else throw new AuthManagerException(ErrorType.INVALID_TOKEN);
        }
    }
}
