package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update")
    public ResponseEntity<String> changeCredentials(@RequestBody ChangeCredentialsRequest request) {
        userService.changeCredentials(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
