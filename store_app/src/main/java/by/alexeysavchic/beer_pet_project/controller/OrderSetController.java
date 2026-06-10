package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.SplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sets")
@RequiredArgsConstructor
public class OrderSetController {
    private final OrderSetService orderSetService;

    @GetMapping
    public List<GetOrderSetResponse> getAllOrderSets() {
        return orderSetService.findAll();
    }

    @PostMapping
    public List<GetOrderSetResponse> getOrderSetsByType(@RequestBody OrderType orderType) {
        return orderSetService.findAllByOrderType(orderType);
    }

    @PostMapping("/mark_split")
    public void markSplit(@RequestBody SplitRequest request) {
        orderSetService.markSplit(request);
    }
}
