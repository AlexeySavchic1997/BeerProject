package by.alexeysavchic.beer_pet_project.entity.id;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.enums.BeerCharacteristic;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class BeerCharacteristicsId implements Serializable
{
    Beer beer;
    BeerCharacteristic characteristic;
}
