package ru.ifmo.monolith.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GatewayAuthenticationFilter extends OncePerRequestFilter {

    private static final String USERNAME_HEADER = "X-User-Name";
    private static final String AUTHORITIES_HEADER = "X-User-Roles";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String username = request.getHeader(USERNAME_HEADER);
        String roles = request.getHeader(AUTHORITIES_HEADER);

        if (username != null && roles != null) {
            List<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                    .map(String::trim)
                    .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    true, true, true, true,
                    authorities
            );
            var auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}