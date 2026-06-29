package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {
    @Size(min = 1, max = 30, message = "sku must be between 1 and 30 symbols")
    @NotBlank
    private String sku;

    @Positive
    private Integer amount;
}
