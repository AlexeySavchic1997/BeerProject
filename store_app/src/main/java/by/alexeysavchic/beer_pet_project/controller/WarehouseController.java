package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<String> getBeerFromWarehouse(@RequestBody @Valid GetWarehouseBeerInfoRequest request) {
        warehouseService.getWarehouseInfo(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
