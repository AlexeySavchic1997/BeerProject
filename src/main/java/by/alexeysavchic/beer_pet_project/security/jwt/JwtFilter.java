package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.security.CustomUserDetails;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(request);
        if(token!=null && jwtService.validateJwtToken(token))
        {
            setCustomUserDetailsToSecurityContextHolder(token);
        }
        filterChain.doFilter(request,response);
    }

    private void setCustomUserDetailsToSecurityContextHolder(String token)
    {
        String email = jwtService.getEmailFromToken(token);
        CustomUserDetails customUserDetails= customUserDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(customUserDetails,
                null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request)
    {
        if (request.getCookies() != null)
        {
            for (Cookie cookie : request.getCookies())
            {
                if ("baseToken".equals(cookie.getName()) || "refreshToken".equals(cookie.getName()))
                {
                        return cookie.getValue();
                }
            }
        }
        return null;
    }
}
