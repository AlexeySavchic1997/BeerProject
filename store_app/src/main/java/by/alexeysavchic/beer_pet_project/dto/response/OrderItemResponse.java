package by.alexeysavchic.beer_pet_project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponse {
    private String beerName;

    private Integer quantity;

    private BigDecimal price;
}
