package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import by.alexeysavchic.beer_pet_project.service.Interface.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createSubscription(@RequestBody @Valid CreateSubscriptionRequest request) {
        subscriptionService.createUserSubscription(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void processSubscriptions(@RequestBody OrderType orderType) {
        orderService.processingSubscriptionOrders(orderType);
    }
}
