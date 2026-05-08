package by.alexeysavchic.beer_pet_project.dto.response;

import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetSubscriptionsResponse {
    private TypeOfSubscription subscriptionType;

    private String description;
}
