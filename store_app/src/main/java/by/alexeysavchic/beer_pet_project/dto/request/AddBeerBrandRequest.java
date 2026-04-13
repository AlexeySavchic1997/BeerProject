package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBeerBrandRequest {
    private String description;

    @NotBlank
    @Size(min=1, max = 30, message = "beer brand name must be between 1 and 30 symbols")
    private String brandName;
}
