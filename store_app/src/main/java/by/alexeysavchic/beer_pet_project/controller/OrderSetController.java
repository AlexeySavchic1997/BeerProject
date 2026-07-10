package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/get")
    public ResponseEntity<List<GetOrderSetResponse>> getAllOrderSets(@RequestBody GetOrderSetsRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(orderSetService.getOrderSets(request));
    }

    @PostMapping("/split")
    public void markSplit(@RequestBody @Valid OrderSetSplitRequest request) {
        orderSetService.markSplit(request);
    }
}
