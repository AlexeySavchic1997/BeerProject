package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderResponse;

public interface OrderService {
    public GetOrderResponse createOrder(CreateOrderRequest request);


}
