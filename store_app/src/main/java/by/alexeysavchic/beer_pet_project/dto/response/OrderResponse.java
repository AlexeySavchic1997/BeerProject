package by.alexeysavchic.beer_pet_project.dto.response;

import by.alexeysavchic.beer_pet_project.entity.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {
    private LocalDateTime orderDate;

    private BigDecimal summaryPrice;

    private OrderStatus status;

    private List<OrderItemResponse> orderItems;
}
