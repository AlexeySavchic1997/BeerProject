package by.alexeysavchic.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateResponseDTO {
    private String sku;

    @Positive
    private Integer amount;
}
