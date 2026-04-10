package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBeerBrandInDBRequest {
    private String description;

    @NotBlank
    @Size(max = 30, message = "beer brand name must be shorter than 30 symbols")
    private String brandName;
}
