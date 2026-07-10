package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import by.alexeysavchic.beer_pet_project.mapper.WarehouseMapper;
import by.alexeysavchic.beer_pet_project.repository.BeerRepository;
import by.alexeysavchic.beer_pet_project.repository.WarehouseRepository;
import by.alexeysavchic.beer_pet_project.service.Implementation.WarehouseServiceImpl;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock
    private ClientService clientService;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private WarehouseMapper warehouseMapper;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Captor
    private ArgumentCaptor<GetWarehouseBeerInfoRequest> requestCaptor;

    @Captor
    private ArgumentCaptor<WarehouseBeerInfo> warehouseInfoCaptor;

    @Nested
    class getUpdatedWarehouseInfoTests {

        @Test
        void successfulUpdateWithNullTimeMark() {
            assertNull(warehouseService.getTimeMark());

            GetWarehouseBeerInfoResponse responseDto = new GetWarehouseBeerInfoResponse();
            WarehouseBeerInfo mappedInfo = new WarehouseBeerInfo();
            mappedInfo.setSku("BEER-123");

            Beer beer = new Beer();
            beer.setSku("BEER-123");

            when(clientService.getWarehouseBeerInfo(any(GetWarehouseBeerInfoRequest.class)))
                    .thenReturn(List.of(responseDto));
            when(warehouseMapper.getWarehouseBeerInfoResponseToWarehouseBeerInfo(responseDto))
                    .thenReturn(mappedInfo);
            when(beerRepository.findBeerBySku("BEER-123"))
                    .thenReturn(Optional.of(beer));

            warehouseService.getUpdatedWarehouseInfo();

            verify(clientService).getWarehouseBeerInfo(requestCaptor.capture());
            assertNotNull(requestCaptor.getValue().getLastModifiedDate());

            verify(warehouseRepository, times(1)).save(warehouseInfoCaptor.capture());
            WarehouseBeerInfo savedInfo = warehouseInfoCaptor.getValue();
            assertEquals(beer, savedInfo.getBeer(), "Найденное пиво должно быть привязано к объекту склада");

            assertNotNull(warehouseService.getTimeMark());
        }

        @Test
        void successfulUpdateWithExistingTimeMark() {

            LocalDateTime pastTime = LocalDateTime.of(2025, 1, 1, 12, 0);
            warehouseService.setTimeMark(pastTime);

            GetWarehouseBeerInfoResponse responseDto = new GetWarehouseBeerInfoResponse();
            WarehouseBeerInfo mappedInfo = new WarehouseBeerInfo();
            mappedInfo.setSku("BEER-456");

            Beer beer = new Beer();
            beer.setSku("BEER-456");

            when(clientService.getWarehouseBeerInfo(any())).thenReturn(List.of(responseDto));
            when(warehouseMapper.getWarehouseBeerInfoResponseToWarehouseBeerInfo(responseDto)).thenReturn(mappedInfo);
            when(beerRepository.findBeerBySku("BEER-456")).thenReturn(Optional.of(beer));

            warehouseService.getUpdatedWarehouseInfo();

            verify(clientService).getWarehouseBeerInfo(requestCaptor.capture());
            assertEquals(pastTime, requestCaptor.getValue().getLastModifiedDate());

            verify(warehouseRepository, times(1)).save(any(WarehouseBeerInfo.class));

            assertNotEquals(pastTime, warehouseService.getTimeMark(), "timeMark должен был обновиться после успешного сохранения");
        }

        @Test
        void doesNotSaveWhenBeerSkuIsUnknown() {
            LocalDateTime pastTime = LocalDateTime.of(2025, 1, 1, 12, 0);
            warehouseService.setTimeMark(pastTime);

            GetWarehouseBeerInfoResponse responseDto = new GetWarehouseBeerInfoResponse();
            WarehouseBeerInfo mappedInfo = new WarehouseBeerInfo();
            mappedInfo.setSku("UNKNOWN-SKU");

            when(clientService.getWarehouseBeerInfo(any())).thenReturn(List.of(responseDto));
            when(warehouseMapper.getWarehouseBeerInfoResponseToWarehouseBeerInfo(responseDto)).thenReturn(mappedInfo);

            when(beerRepository.findBeerBySku("UNKNOWN-SKU")).thenReturn(Optional.empty());

            warehouseService.getUpdatedWarehouseInfo();

            verify(warehouseRepository, never()).save(any());

            assertEquals(pastTime, warehouseService.getTimeMark(), "timeMark не должен обновляться, если сохранение не произошло");
        }
    }

    @Nested
    class getWarehouseInfoTests {

        @Test
        void successfulGetWarehouseInfoTest() {
            GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest();
            GetWarehouseBeerInfoResponse response = new GetWarehouseBeerInfoResponse();
            List<GetWarehouseBeerInfoResponse> expectedList = List.of(response);

            when(clientService.getWarehouseBeerInfo(request)).thenReturn(expectedList);

            List<GetWarehouseBeerInfoResponse> result = warehouseService.getWarehouseInfo(request);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(expectedList, result);

            verify(clientService, times(1)).getWarehouseBeerInfo(request);
        }
    }
}
