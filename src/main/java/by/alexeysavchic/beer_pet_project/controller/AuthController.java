package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtDTO;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<JwtDTO> signUp(@RequestBody UserRegisterRequest request)
    {
        JwtDTO tokens= authService.signUp(request);

        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> logIn(@RequestBody LogInRequest request)
    {
        JwtDTO tokens= authService.logIn(request);

        return ResponseEntity.status(HttpStatus.OK).body(tokens);
    }

    @GetMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK).body("baseToken: "+ authService.refresh(request));
    }



}
