package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerInDBRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetBeerRequest;
import by.alexeysavchic.beer_pet_project.dto.response.BeerResponse;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beer")
public class BeerController {
    private final BeerService beerService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<String> addBeer(@RequestBody AddBeerInDBRequest newBeer) {
        beerService.addNewBeer(newBeer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/get")
    public Page<BeerResponse> getBeers(@RequestBody(required = false) GetBeerRequest request, @PageableDefault(size = 20) Pageable pageable) {
        return beerService.findAll(request, pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeer(@PathVariable("sku") String sku) {
        beerService.deleteBeer(sku);
    }
}
