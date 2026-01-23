package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.security.CookieJwtConfig;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController
{
    private final UserService userService;

    private final CookieJwtConfig config;

    public AuthController(UserService userService, CookieJwtConfig config) {
        this.userService = userService;
        this.config = config;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRegisterRequest request)
    {
        userService.signUp(request);
        return ResponseEntity.ok("Register was successful");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody LogInRequest request, HttpServletResponse response)
    {
       String jwt=userService.logIn(request);

        ResponseCookie cookie = ResponseCookie.from(config.getCookie().getName(), jwt)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(config.getCookie().getExpiresIn())
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/test")
    public void test()
    {
        System.out.println("success");
    }

}
