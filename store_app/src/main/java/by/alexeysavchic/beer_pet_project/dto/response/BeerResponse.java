package by.alexeysavchic.beer_pet_project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BeerResponse {
    private String name;

    private String description;

    private BigDecimal volume;

    private BigDecimal price;

    private String beerBrand;

    private List<CharacteristicsResponse> characteristics;
}
