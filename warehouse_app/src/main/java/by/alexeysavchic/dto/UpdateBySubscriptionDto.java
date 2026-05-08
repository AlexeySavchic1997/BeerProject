package by.alexeysavchic.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UpdateBySubscriptionDto {
    private Long userId;

    private List<UpdateWarehouseDTO> updateWarehouseDTOS;
}
