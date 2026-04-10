package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
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
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final ClientService clientService;

    private final WarehouseRepository warehouseRepository;

    private final BeerRepository beerRepository;

    private final WarehouseMapper mapper;

    private final Logger logger = LogManager.getLogger(WarehouseServiceImpl.class);

    private LocalDateTime timeMark;

    @Override
    public void getUpdatedWarehouseInfo() {
        if (timeMark == null) {
            timeMark = LocalDateTime.now().minusMonths(1);
        }
        GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest();
        request.setLastModifiedDate(timeMark);
        List<GetWarehouseBeerInfoResponse> listDTO = clientService.getWarehouseBeerInfo(request);

        for (GetWarehouseBeerInfoResponse response : listDTO) {
            WarehouseBeerInfo warehouseInfo = mapper.getWarehouseBeerInfoResponseToWarehouseBeerInfo(response);
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

    @Override
    public void updateWarehouseInfo(@Valid UpdateWarehouseInfoDTO updateDTO) {
        clientService.updateWarehouseInfo(updateDTO);
        timeMark = LocalDateTime.now();
    }

}
