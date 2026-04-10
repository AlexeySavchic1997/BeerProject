package by.alexeysavchic.beer_pet_project.dto.response;

import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class CharacteristicsResponse {
    private BeerCharacteristic characteristic;

    private BigDecimal value;
}
