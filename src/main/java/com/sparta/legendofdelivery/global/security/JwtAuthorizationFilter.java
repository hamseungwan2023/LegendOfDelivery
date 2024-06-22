package com.sparta.legendofdelivery.global.security;

import com.sparta.legendofdelivery.domain.user.security.UserDetailsServiceImpl;
import com.sparta.legendofdelivery.global.dto.SecurityResponse;
import com.sparta.legendofdelivery.global.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final SecurityResponse securityResponse;

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService, SecurityResponse securityResponse) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.securityResponse = securityResponse;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtProvider.getAccessTokenFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {

            try {

                Claims info = jwtProvider.getClaimsFromToken(tokenValue);
                setAuthentication(info.getSubject());

            } catch (ExpiredJwtException e) {
                securityResponse.sendResponse(response, HttpStatus.UNAUTHORIZED, "만료된 토큰 입니다.");

                return;
            } catch (JwtException e) {
                securityResponse.sendResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 입니다.");

                return;
            }

        }

        filterChain.doFilter(request, response);

    }

    public void setAuthentication(String userId) {

        Authentication authentication = createAuthentication(userId);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

    }

    private Authentication createAuthentication(String userId) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
