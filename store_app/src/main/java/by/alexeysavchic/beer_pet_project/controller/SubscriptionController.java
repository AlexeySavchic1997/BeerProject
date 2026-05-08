package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.service.Interface.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<String> createSubscription(CreateSubscriptionRequest request) {
        subscriptionService.createUserSubscription(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
