package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.WarehouseBeerInfoItem;
import warehouse_api.Zone;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GrpcMapperTest {

    private final GrpcMapper mapper = Mappers.getMapper(GrpcMapper.class);

    @Test
    void shouldMapLocalDateTimeToTimestampInUtc() {
        LocalDateTime testTime = LocalDateTime.of(2026, 7, 7, 12, 0, 0);
        GetWarehouseBeerInfoRequest javaRequest = new GetWarehouseBeerInfoRequest();
        javaRequest.setLastModifiedDate(testTime);

        GetWarehouseInfoRequest grpcRequest = mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(javaRequest);

        assertNotNull(grpcRequest);
        assertTrue(grpcRequest.hasTime());

        long expectedSeconds = testTime.toInstant(ZoneOffset.UTC).getEpochSecond();
        assertEquals(expectedSeconds, grpcRequest.getTime().getSeconds());
    }

    @Test
    void shouldMapUnrecognizedZoneToNull() {

        WarehouseBeerInfoItem grpcItem = WarehouseBeerInfoItem.newBuilder()
                .setId(1L)
                .setSku("TEST_SKU")
                .setAmount(100)
                .setZoneTypeValue(5)
                .build();

        GetWarehouseBeerInfoResponse javaResponse = mapper.WarehouseInfoDTOToWarehouseInfoBeerDTOResponse(grpcItem);

        assertNotNull(javaResponse);
        assertEquals(1L, javaResponse.getId());
        assertEquals("TEST_SKU", javaResponse.getSku());
        assertEquals(100, javaResponse.getAmount());
        assertNull(javaResponse.getZoneType());
    }

    @Test
    void shouldMapNormalZoneCorrectly() {

        WarehouseBeerInfoItem grpcItem = WarehouseBeerInfoItem.newBuilder()
                .setZoneType(Zone.ZONE_SORTING)
                .build();

        GetWarehouseBeerInfoResponse javaResponse = mapper.WarehouseInfoDTOToWarehouseInfoBeerDTOResponse(grpcItem);

        assertNotNull(javaResponse);
        assertEquals(Zone.ZONE_SORTING, javaResponse.getZoneType());
    }
}
