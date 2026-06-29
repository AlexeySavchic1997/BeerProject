package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<GetOrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request) {
        GetOrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
}
