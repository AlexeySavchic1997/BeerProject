package by.alexeysavchic.beer_pet_project.entity.id;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class BeerOfTheMonthId implements Serializable {
    private Short month;
    private Short year;
}
