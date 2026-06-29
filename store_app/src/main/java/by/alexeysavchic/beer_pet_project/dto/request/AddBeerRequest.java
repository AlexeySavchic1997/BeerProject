package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBeerRequest {
    @Size(min = 1, max = 30, message = "sku must be between 1 and 30 symbols")
    @NotBlank
    private String sku;

    @Size(min = 1, max = 20, message = "beer name must be between 2 and 20 symbols")
    @NotBlank
    private String name;

    private String description;

    @Positive(message = "volume must be positive")
    private BigDecimal volume;

    @Positive(message = "price must be positive")
    private BigDecimal price;

    private String beerBrand;

    private List<AddBeerCharacteristicRequest> characteristics;
}
