package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final OrderService orderService;

    @PatchMapping
    public void processSubscriptions(@RequestBody OrderType orderType) {
        orderService.processingSubscriptionOrders(orderType);
    }
}
