package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;

public interface OrderService {
    public void createOrder(CreateOrderRequest request);

    public void processingSubscriptionOrders(OrderType type);

    public void saveOrdersFromSubscriptions();
}
