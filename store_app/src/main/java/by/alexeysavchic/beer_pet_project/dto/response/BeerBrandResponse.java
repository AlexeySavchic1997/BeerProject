package by.alexeysavchic.beer_pet_project.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeerBrandResponse {
    private String description;

    @NotBlank
    private String brandName;
}
