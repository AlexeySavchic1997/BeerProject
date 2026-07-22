package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateBatchRequest {
    @Min(value = 10, message = "count of orders must be more or equal than 10")
    @Max(value = 100, message = "count of orders must be less or equal than 100")
    private Integer count;

    @Positive
    private Long setId;
}
