package by.alexeysavchic.beer_pet_project.entity.id;


import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class OrderItemId implements Serializable
{
    private Order order;
    private Beer beer;
}
