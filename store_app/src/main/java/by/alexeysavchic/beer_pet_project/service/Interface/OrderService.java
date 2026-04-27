package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.User;

import java.time.LocalDateTime;

public interface OrderService
{
    public Order createOrder(CartOrderRequest request, LocalDateTime timeMark, User user);

    public void cancelOrder(CartOrderRequest request, Long orderId);
}
