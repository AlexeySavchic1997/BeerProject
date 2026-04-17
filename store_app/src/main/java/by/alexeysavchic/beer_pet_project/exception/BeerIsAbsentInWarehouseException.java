package by.alexeysavchic.beer_pet_project.exception;

import java.util.List;
import java.util.Set;

public class BeerIsAbsentInWarehouseException extends RuntimeException
{
    public BeerIsAbsentInWarehouseException(List<Long> ids) {
        super(String.format(ErrorMessages.beerIsAbsentInWarehouse, ids));
    }
}
