package com.trialwork.swedbank.banking.config.security;

import java.util.*;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private final JwtProperties jwtProperties;
    private RSAPublicKey publicKey;

    @PostConstruct
    public void initPublicKey() {
        try {
            final var keyBytes = Base64.getDecoder().decode(jwtProperties.getPublicKey());
            final var spec = new X509EncodedKeySpec(keyBytes);
            publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to parse RSA public key", e);
        }
    }

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        final var authHeader = request.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final var token = authHeader.substring(BEARER_PREFIX.length());

        try {
            final var claims = Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            final var userCode = claims.getSubject();
            final var authentication = new UsernamePasswordAuthenticationToken(userCode, null, List.of());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (final ExpiredJwtException e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token has expired");
        } catch (final JwtException e) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }
    }

    private void sendError(
        final HttpServletResponse response,
        final int status,
        final String message
    ) throws IOException {
        response.setStatus(status);
        response.setContentType(CONTENT_TYPE_JSON);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}