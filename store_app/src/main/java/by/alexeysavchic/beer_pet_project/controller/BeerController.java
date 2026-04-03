package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.AddNewBeer;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class BeerController
{
    private final BeerService beerService;

    @PostMapping("/add_beer")
    public ResponseEntity<String> addBeer(AddNewBeer newBeer)
    {
        beerService.addNewBeer(newBeer);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePoll (@PathVariable("sku") String sku)
    {
        beerService.deleteBeer(sku);
    }
}
