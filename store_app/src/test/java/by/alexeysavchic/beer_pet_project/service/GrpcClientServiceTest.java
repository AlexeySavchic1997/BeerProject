package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.exception.WarehouseServerException;
import by.alexeysavchic.beer_pet_project.mapper.GrpcMapper;
import by.alexeysavchic.beer_pet_project.service.Implementation.GrpcClientService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.WarehouseApiGrpc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrpcClientServiceTest {

    @Mock
    private GrpcMapper mapper;

    @Mock
    private WarehouseApiGrpc.WarehouseApiBlockingStub blockingStub;

    @InjectMocks
    private GrpcClientService grpcClientService;

    @BeforeEach
    void setUp() {
        grpcClientService.setBlockingStub(blockingStub);
    }

    @Nested
    class getWarehouseBeerInfoTests {

        @Test
        void successfulGetWarehouseBeerInfoTest() {
            GetWarehouseBeerInfoRequest javaRequest = new GetWarehouseBeerInfoRequest();

            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder().build();
            BeerInfoResponse grpcResponse = BeerInfoResponse.newBuilder().build();

            GetWarehouseBeerInfoResponse javaResponse = new GetWarehouseBeerInfoResponse();

            when(mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(javaRequest)).thenReturn(grpcRequest);
            when(blockingStub.getWarehouseInfo(grpcRequest)).thenReturn(grpcResponse);
            when(mapper.listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(grpcResponse.getBeerList()))
                    .thenReturn(List.of(javaResponse));

            List<GetWarehouseBeerInfoResponse> result = grpcClientService.getWarehouseBeerInfo(javaRequest);

            assertNotNull(result);
            assertEquals(1, result.size());

            verify(blockingStub, times(1)).getWarehouseInfo(grpcRequest);
        }

        @Test
        void throwsExceptionWhenGrpcServerIsDown() {

            GetWarehouseBeerInfoRequest javaRequest = new GetWarehouseBeerInfoRequest();
            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder().build();

            when(mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(javaRequest)).thenReturn(grpcRequest);

            StatusRuntimeException grpcException = new StatusRuntimeException(Status.UNAVAILABLE.withDescription("Server is down"));
            when(blockingStub.getWarehouseInfo(grpcRequest)).thenThrow(grpcException);

            WarehouseServerException thrown = assertThrows(WarehouseServerException.class,
                    () -> grpcClientService.getWarehouseBeerInfo(javaRequest));

            assertTrue(thrown.getMessage().contains("UNAVAILABLE"));

            verify(mapper, never()).listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(any());
        }
    }
}