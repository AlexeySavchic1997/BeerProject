package by.alexeysavchic.beer_pet_project.dto.response;

import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.EnumMap;

@Getter
@Setter
@NoArgsConstructor
public class GetOrderSetResponse {
    private Long id;

    private Integer commonQuantity;

    private OrderSetStatus status;

    private EnumMap<Gender, Integer> genderSplit;

    private EnumMap<Location, Integer> locationSplit;
}
