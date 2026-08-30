package com.igor.EcoPathAPI.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final HandlerExceptionResolver resolver;

    public SecurityFilter(TokenService tokenService,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.tokenService = tokenService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> token = extractToken(request);

            if (token.isPresent()) {
                authenticateClient(token.get());
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            resolver.resolveException(request, response, null, ex);
        }
    }

    private Optional<String> extractToken(HttpServletRequest request){
        var headerAuthorization = request.getHeader("Authorization");

        if(headerAuthorization == null || headerAuthorization.isBlank() || !headerAuthorization.startsWith("Bearer ")){
            return Optional.empty();
        }

        String token = headerAuthorization.replace("Bearer ", "");
        return Optional.of(token);
    }

    private void authenticateClient(String token){
        String subject = tokenService.validateToken(token);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(subject, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


}
