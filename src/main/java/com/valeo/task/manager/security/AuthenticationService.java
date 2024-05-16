package com.valeo.task.manager.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.http.HttpServletRequest;

public class AuthenticationService {

    private static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final String USER_AUTH_TOKEN = "userkey";
    private static final String ADMIN_AUTH_TOKEN = "adminkey";

    public static Authentication getAuthentication(HttpServletRequest request) {
        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null) {
            throw new BadCredentialsException("Invalid API Key");
        }
        if(!apiKey.equals(USER_AUTH_TOKEN) && !apiKey.equals(ADMIN_AUTH_TOKEN)) {
        	throw new BadCredentialsException("Invalid API Key");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if(apiKey.equals(USER_AUTH_TOKEN)) grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if(apiKey.equals(ADMIN_AUTH_TOKEN)) grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return new ApiKeyAuthentication(apiKey, grantedAuthorities);
//        return new ApiKeyAuthentication(apiKey, AuthorityUtils.NO_AUTHORITIES);
    }
}
