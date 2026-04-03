package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;

import java.util.List;

public interface ClientService
{
    public List<GetWarehouseBeerInfoResponse> getWarehouseBeerInfo(GetWarehouseBeerInfoRequest request);

    public void updateWarehouseInfo(UpdateWarehouseInfoDTO updateWarehouseInfoDTO);
}
