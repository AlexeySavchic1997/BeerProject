package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetBeerBrandResponse;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/beer/brand")
@RequiredArgsConstructor
public class BeerBrandController {
    private final BeerBrandService beerBrandService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> addNewBeerBrand(@RequestBody @Valid AddBeerBrandRequest newBeerBrand) {
        beerBrandService.addBeerBrand(newBeerBrand);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<Page<GetBeerBrandResponse>> getBeerBrands(
            @RequestParam(required = false, name = "name") String name, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(beerBrandService.getBeerBrands(name, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{name}")
    public ResponseEntity<String> deleteBeerBrand(@PathVariable("name") String name) {
        beerBrandService.deleteBeerBrand(name);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
