package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import by.alexeysavchic.beer_pet_project.exception.BeerIsAbsentInWarehouseException;
import by.alexeysavchic.beer_pet_project.mapper.GrpcMapper;
import by.alexeysavchic.beer_pet_project.mapper.WarehouseMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.WarehouseRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ClientService clientService;

    private final WarehouseRepository warehouseRepository;

    private final BeerRepository beerRepository;

    private final WarehouseMapper warehouseMapper;

    private final Logger logger = LogManager.getLogger(WarehouseServiceImpl.class);

    private LocalDateTime timeMark;

    @Override
    public void getUpdatedWarehouseInfo() {
        if (timeMark == null) {
            timeMark = LocalDateTime.now();
        }
        GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest();
        request.setLastModifiedDate(timeMark);
        List<GetWarehouseBeerInfoResponse> listDTO = clientService.getWarehouseBeerInfo(request);

        for (GetWarehouseBeerInfoResponse response : listDTO) {
            WarehouseBeerInfo warehouseInfo = warehouseMapper.getWarehouseBeerInfoResponseToWarehouseBeerInfo(response);
            beerRepository.findBeerBySku(warehouseInfo.getSku()).ifPresentOrElse(
                    beer ->
                    {
                        warehouseInfo.setBeer(beer);
                        warehouseRepository.save(warehouseInfo);
                    },
                    () -> {
                        logger.error("cannot add this beer in DB with unknown sku: " + warehouseInfo.getSku());
                    }
            );
        }

    }

    @Override
    public List<GetWarehouseBeerInfoResponse> getWarehouseInfo(@Valid GetWarehouseBeerInfoRequest request) {
        return clientService.getWarehouseBeerInfo(request);
    }

    public void updateWarehouseInfo(CartOrderRequest request, LocalDateTime timeMark)
    {
        List<OrderItemRequest> cart = request.getCart();
        List<Long> keyList=new ArrayList<>();
        for(OrderItemRequest item:cart)
        {
            keyList.add(item.getId());
        }
        List<WarehouseBeerInfo> warehouseList=warehouseRepository.findAllByBeerIdInSortingAndUnloadingZone(keyList);
        Map<Long, WarehouseBeerInfo> sorting = new HashMap<>();
        Map<Long, WarehouseBeerInfo> unloading = new HashMap<>();
        for (WarehouseBeerInfo info : warehouseList) {
            if (info.getZoneType()==(ZoneType.ZONE_SORTING)) {
                sorting.put(info.getBeer().getId(), info);
            } else {
                unloading.put(info.getBeer().getId(), info);
            }
        }
        List<UpdateWarehouseInfoDTO> updateList = new ArrayList<>();
        for (OrderItemRequest item:cart)
        {
            Long id=item.getId();
            WarehouseBeerInfo sortingBeer = sorting.get(id);
            WarehouseBeerInfo unloadingBeer = unloading.get(id);
            UpdateWarehouseInfoDTO sortingUpdate=warehouseMapper.warehouseBeerInfoToUpdateWarehouseInfoDTO(sortingBeer);
            UpdateWarehouseInfoDTO unloadingUpdate=warehouseMapper.warehouseBeerInfoToUpdateWarehouseInfoDTO(unloadingBeer);
            sortingUpdate.setTimeMark(timeMark);
            unloadingUpdate.setTimeMark(timeMark);
            sortingUpdate.setPlus(false);
            unloadingUpdate.setPlus(true);
            updateList.add(sortingUpdate);
            updateList.add(unloadingUpdate);
        }
        clientService.updateWarehouseInfo(updateList);
        this.timeMark=timeMark;
    }


}
