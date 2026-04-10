package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetBeerRequest {
    private Long id;

    @Size(max = 30, message = "unvalid sku")
    private String sku;

    @Size(min = 2, max = 20, message = "beer name must be between 2 and 20 symbols")
    private String name;

    private String description;

    @Positive
    private BigDecimal volume;

    @Positive
    private BigDecimal lowerPrice;

    @Positive
    private BigDecimal upperPrice;

    private String beerBrandName;

    private List<BeerCharacteristicsRequest> characteristics;
}
