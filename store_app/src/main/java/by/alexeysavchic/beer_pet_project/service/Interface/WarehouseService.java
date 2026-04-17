package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface WarehouseService {
    public void getUpdatedWarehouseInfo();

    public List<GetWarehouseBeerInfoResponse> getWarehouseInfo(GetWarehouseBeerInfoRequest request);

    public void sendOrderToWarehouse(CartOrderRequest request, LocalDateTime timeMark);

    public void cancelOrderToWarehouse(CartOrderRequest request, LocalDateTime timeMark);
}
