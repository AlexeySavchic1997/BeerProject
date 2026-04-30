package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;

import java.util.List;

public interface WarehouseService {
    public void getUpdatedWarehouseInfo();

    public List<GetWarehouseBeerInfoResponse> getWarehouseInfo(GetWarehouseBeerInfoRequest request);
}
