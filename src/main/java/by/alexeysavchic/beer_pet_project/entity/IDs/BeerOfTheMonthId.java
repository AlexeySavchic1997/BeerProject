package by.alexeysavchic.beer_pet_project.entity.IDs;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class BeerOfTheMonthId implements Serializable
{
    Byte month;
    Integer year;
}
