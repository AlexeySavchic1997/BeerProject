package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class CartOrderRequest
{
    Map<@Positive Long, @Positive Integer> cart;
}
