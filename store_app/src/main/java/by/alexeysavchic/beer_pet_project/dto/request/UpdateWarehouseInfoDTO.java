package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import warehouse_api.Zone;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateWarehouseInfoDTO {
    @PositiveOrZero
    private Long id;

    @PositiveOrZero
    private Integer amount;

    private boolean isAdding;

    @PastOrPresent
    private LocalDateTime timeMark;
}
