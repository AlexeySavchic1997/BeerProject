package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateWarehouseInfoDTO {

    @Size(min = 1, max = 30, message = "sku must be between 1 and 30 symbols")
    private String sku;

    @PositiveOrZero
    private Integer amount;
}
