package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class AddBeerCharacteristicRequest {
    private BeerCharacteristic characteristic;

    @Positive
    private BigDecimal value;
}
