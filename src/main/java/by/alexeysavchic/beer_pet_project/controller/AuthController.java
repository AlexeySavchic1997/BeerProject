package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtDTO;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRegisterRequest request, HttpServletResponse response)
    {
        JwtDTO jwt= authService.signUp(request);

        ResponseCookie baseCookie =createBaseCookie(jwt.getBaseToken());
        ResponseCookie refreshCookie = createRefreshCookie(jwt.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body("Register was successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody LogInRequest request, HttpServletResponse response)
    {
        JwtDTO jwt= authService.logIn(request);

        ResponseCookie baseCookie =createBaseCookie(jwt.getBaseToken());
        ResponseCookie refreshCookie = createRefreshCookie(jwt.getRefreshToken());

        response.addHeader(HttpHeaders.SET_COOKIE, baseCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body("Login was successful");
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response)
    {
        for (Cookie cookie : request.getCookies())
        {
            if ("refreshToken".equals(cookie.getName()))
            {
                ResponseCookie baseCookie = createBaseCookie(authService.refresh(cookie.getValue()));
                response.addHeader(HttpHeaders.SET_COOKIE, baseCookie.toString());
                break;
            }
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseCookie createBaseCookie(String token)
    {
          return ResponseCookie.from("baseToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(900)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();
    }

    private ResponseCookie createRefreshCookie(String token)
    {
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/refresh")
                .maxAge(86400)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();
    }
}
