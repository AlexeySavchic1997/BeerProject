package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.exception.ExpiredJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.WrongTokenTypeException;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetails;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private HandlerExceptionResolver exceptionResolver;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtFilter jwtFilter;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Nested
    class doFilterInternalTests {

        @Test
        void proceedsWithoutAuthenticationIfNoTokenProvided() throws ServletException, IOException {

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());

            verify(jwtService, never()).getTypeFromToken(any());
        }

        @Test
        void proceedsWithoutAuthenticationIfHeaderDoesNotStartWithBearer() throws ServletException, IOException {
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Basic some_token");

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void successfullyAuthenticatesWithValidBaseToken() throws ServletException, IOException {
            String token = "valid_base_token";
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
            when(request.getRequestURI()).thenReturn("/api/v1/orders");

            when(jwtService.getTypeFromToken(token)).thenReturn("Base");
            when(jwtService.validateJwtToken(token)).thenReturn(true);
            when(jwtService.getEmailFromToken(token)).thenReturn("user@gmail.com");

            CustomUserDetails mockUserDetails = mock(CustomUserDetails.class);
            when(mockUserDetails.getAuthorities()).thenReturn(Collections.emptyList());
            when(customUserDetailsService.loadUserByUsername("user@gmail.com")).thenReturn(mockUserDetails);

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertNotNull(authentication);
            assertEquals(mockUserDetails, authentication.getPrincipal());
        }

        @Test
        void delegatesToExceptionResolverWhenBaseTokenSentToRefreshEndpoint() throws ServletException, IOException {
            String token = "base_token";
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
            when(request.getRequestURI()).thenReturn("/api/v1/auth/refresh");
            when(jwtService.getTypeFromToken(token)).thenReturn("Base");

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(exceptionResolver, times(1)).resolveException(
                    eq(request), eq(response), isNull(), any(WrongTokenTypeException.class));

            verify(filterChain, never()).doFilter(any(), any());
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        void delegatesToExceptionResolverWhenRefreshTokenSentToNormalEndpoint() throws ServletException, IOException {
            String token = "refresh_token";
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
            when(request.getRequestURI()).thenReturn("/api/v1/users/me");
            when(jwtService.getTypeFromToken(token)).thenReturn("Refresh");

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(exceptionResolver, times(1)).resolveException(
                    eq(request), eq(response), isNull(), any(WrongTokenTypeException.class));

            verify(filterChain, never()).doFilter(any(), any());
        }

        @Test
        void delegatesToExceptionResolverWhenTokenIsExpired() throws ServletException, IOException {
            String token = "expired_token";
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
            when(request.getRequestURI()).thenReturn("/api/v1/orders");
            when(jwtService.getTypeFromToken(token)).thenReturn("Base");

            when(jwtService.validateJwtToken(token)).thenThrow(new ExpiredJwtTokenException());

            jwtFilter.doFilterInternal(request, response, filterChain);

            verify(exceptionResolver, times(1)).resolveException(
                    eq(request), eq(response), isNull(), any(ExpiredJwtTokenException.class));

            verify(filterChain, never()).doFilter(any(), any());
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }
    }
}
