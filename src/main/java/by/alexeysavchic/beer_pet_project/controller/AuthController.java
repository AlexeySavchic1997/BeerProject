package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.CookieDTO;
import by.alexeysavchic.beer_pet_project.exception.RefreshCookieIsAbsentException;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController
{
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRegisterRequest request, HttpServletResponse response)
    {
        CookieDTO cookies= authService.signUp(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookies.getBaseCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookies.getRefreshCookie().toString());

        return ResponseEntity.status(HttpStatus.OK).body("Register was successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody LogInRequest request, HttpServletResponse response)
    {
        CookieDTO cookies= authService.logIn(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookies.getBaseCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookies.getRefreshCookie().toString());

        return ResponseEntity.status(HttpStatus.OK).body("Login was successful");
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie requestCookie=Arrays.stream(request.getCookies()).filter(cookie -> cookie.
                getName().equals("refreshToken")).findFirst().orElseThrow(()->new RefreshCookieIsAbsentException());

                ResponseCookie baseCookie = authService.refresh(requestCookie.getValue());
                response.addHeader(HttpHeaders.SET_COOKIE, baseCookie.toString());

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
