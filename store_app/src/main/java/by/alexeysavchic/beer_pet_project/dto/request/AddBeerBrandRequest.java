package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddBeerBrandRequest {
    @NotBlank
    @Size(min = 1, max = 30, message = "beer brand name must be between 1 and 30 symbols")
    private String brandName;

    private String description;
}
