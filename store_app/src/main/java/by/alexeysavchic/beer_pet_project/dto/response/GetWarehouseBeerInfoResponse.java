package by.alexeysavchic.beer_pet_project.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import warehouse_api.Zone;

@Getter
@Setter
@NoArgsConstructor
public class GetWarehouseBeerInfoResponse {
    private Long id;

    private String sku;

    private Integer amount;

    private Zone zoneType;
}
