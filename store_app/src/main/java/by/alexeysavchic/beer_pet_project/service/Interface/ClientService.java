package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import warehouse_api.UnpassedOrderResponse;

import java.util.List;

public interface ClientService {
    public List<GetWarehouseBeerInfoResponse> getWarehouseBeerInfo(GetWarehouseBeerInfoRequest request);

    public List<UnpassedOrderResponse> updateWarehouseInfo(List<OrderItemRequest> updates);
}
