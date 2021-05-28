package com.example04.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example04.model.UserEntity;
import com.example04.service.CustomUserDetailsService;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
//        try {
//            String token = getJwtFromRequest(request);
//            if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
//                Long userId = jwtProvider.getUserIdFromJwt(token);
//                UserEntity user = (UserEntity) userDetailsService.loadUserById(userId);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,
//                        user.getRoles(), user.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (Exception ex) {
//            log.info("Impossible to set user authentication in security context");
//        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtProvider.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());
        }
        return null;
    }
}
