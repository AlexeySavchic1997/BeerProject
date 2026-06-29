package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.SplitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrderSetsRequest {
    private OrderSetStatus orderSetStatus;

    private SplitType splitType;

    private OrderType orderType;
}
