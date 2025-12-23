package by.alexeysavchic.beer_pet_project.entity.IDs;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode
public class BeerCharacteristicsId
{
    Beer beer;
    BeerCharacteristic characteristic;
}
