package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest
{
    @PositiveOrZero
    Long id;

    @Positive
    Integer amount;
}
