package by.alexeysavchic.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateWarehouseDTO {
    @PositiveOrZero
    private Long id;

    @PositiveOrZero
    private Integer amount;
}
