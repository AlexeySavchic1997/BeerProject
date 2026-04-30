package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class AddBeerRequest {
    @Size(min = 1, max = 30, message = "sku must be between 1 and 30 symbols")
    @NotBlank
    private String sku;

    @Size(min = 1, max = 20, message = "beer name must be between 2 and 20 symbols")
    @NotBlank
    private String name;

    private String description;

    @Positive
    private BigDecimal volume;

    @Positive
    private BigDecimal price;

    private String beerBrand;

    private List<AddBeerCharacteristicRequest> characteristics;
}
