package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class CreateSubscriptionRequest {

    private TypeOfSubscription typeOfSubscription;

    @Min(value = 1, message = "Duration should not be less than 1")
    @Max(value = 12, message = "Duration should not be greater than 12")
    private Integer durationMonths;

    private List<String> listSKU;
}
