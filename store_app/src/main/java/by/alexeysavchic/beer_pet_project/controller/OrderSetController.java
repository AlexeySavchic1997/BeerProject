package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/set")
@RequiredArgsConstructor
public class OrderSetController {
    private final OrderSetService orderSetService;

    @GetMapping
    public List<GetOrderSetResponse> getAllOrderSets(GetOrderSetsRequest request) {
        return orderSetService.getOrderSets(request);
    }

    @PostMapping("/split")
    public void markSplit(@RequestBody OrderSetSplitRequest request) {
        orderSetService.markSplit(request);
    }
}
