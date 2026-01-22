package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.RefreshTokenRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtAuthentificationDTO;
import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController
{
    UserService userService;

    public AuthController(UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody UserRegisterRequest request)
    {
        userService.signUp(request);
        return ResponseEntity.ok("Register was successful");
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseCookie> logIn(@Valid @RequestBody LogInRequest request)
    {
        return ResponseEntity.ok(userService.logIn(request));
    }

}
