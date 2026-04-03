package by.alexeysavchic.beer_pet_project.dto.request;

import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class GetWarehouseBeerInfoRequest
{
    @PositiveOrZero(message = "Id can't be negative")
    private Long id;

    private ZoneType zoneType;

    private String sku;

    @PositiveOrZero(message = "Amount of items can't be negative")
    private Integer amount;

    @PastOrPresent(message = "Date of modifications can't be in future")
    private LocalDateTime lastModifiedDate;
}
