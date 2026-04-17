package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Order;

import java.time.LocalDateTime;

public interface OrderService
{
    public Long createOrder(CartOrderRequest request, LocalDateTime timeMark);

    public void cancelOrder(CartOrderRequest request, Long orderId);
}
