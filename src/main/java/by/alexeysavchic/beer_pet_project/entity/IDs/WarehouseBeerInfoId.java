package by.alexeysavchic.beer_pet_project.entity.IDs;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class WarehouseBeerInfoId implements Serializable
{
    ZoneType zoneType;
    Beer beer;
}
