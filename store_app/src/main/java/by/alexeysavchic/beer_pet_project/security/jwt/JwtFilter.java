package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.exception.WrongTokenTypeException;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetails;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final HandlerExceptionResolver exceptionResolver;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService customUserDetailsService,
                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if (token != null &&
                    ((!request.getRequestURI().equals("/api/v1/auth/refresh") && jwtService.getTypeFromToken(token).equals("Refresh"))
                            || (request.getRequestURI().equals("/api/v1/auth/refresh") && !jwtService.getTypeFromToken(token).equals("Refresh")))) {
                String expectedType = (request.getRequestURI().equals("/api/v1/auth/refresh") ? "Refresh" : "Base");
                throw new WrongTokenTypeException(jwtService.getTypeFromToken(token), expectedType);
            }
            if (token != null && jwtService.validateJwtToken(token)) {
                setCustomUserDetailsToSecurityContextHolder(token);
            }
            filterChain.doFilter(request, response);
        } catch (RuntimeException ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private void setCustomUserDetailsToSecurityContextHolder(String token) {
        String email = jwtService.getEmailFromToken(token);
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
