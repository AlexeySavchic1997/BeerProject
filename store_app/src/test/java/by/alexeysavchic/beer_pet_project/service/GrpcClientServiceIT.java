package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.exception.WarehouseServerException;
import by.alexeysavchic.beer_pet_project.mapper.GrpcMapper;
import by.alexeysavchic.beer_pet_project.service.Implementation.GrpcClientService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.Status;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.WarehouseApiGrpc;
import warehouse_api.WarehouseBeerInfoItem;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GrpcClientServiceIT {

    private Server inProcessServer;
    private ManagedChannel inProcessChannel;

    private GrpcClientService grpcClientService;

    @Mock
    private GrpcMapper mapper;

    @BeforeEach
    void setUp() throws IOException {

        String serverName = InProcessServerBuilder.generateName();

        WarehouseApiGrpc.WarehouseApiImplBase fakeWarehouseService = new WarehouseApiGrpc.WarehouseApiImplBase() {
            @Override
            public void getWarehouseInfo(GetWarehouseInfoRequest request, StreamObserver<BeerInfoResponse> responseObserver) {
                if ("ERROR".equals(request.getSku())) {
                    responseObserver.onError(Status.UNAVAILABLE.withDescription("Warehouse is down").asRuntimeException());
                    return;
                }

                WarehouseBeerInfoItem beerInfo = WarehouseBeerInfoItem.newBuilder()
                        .setSku("BEER-INTEGRATION-1")
                        .setAmount(100)
                        .build();

                BeerInfoResponse response = BeerInfoResponse.newBuilder()
                        .addBeer(beerInfo)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };

        inProcessServer = InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(fakeWarehouseService)
                .build()
                .start();

        inProcessChannel = InProcessChannelBuilder
                .forName(serverName)
                .directExecutor()
                .build();

        grpcClientService = new GrpcClientService(mapper);
        grpcClientService.setBlockingStub(WarehouseApiGrpc.newBlockingStub(inProcessChannel));
    }

    @AfterEach
    void tearDown() {
        if (inProcessChannel != null) {
            inProcessChannel.shutdownNow();
        }
        if (inProcessServer != null) {
            inProcessServer.shutdownNow();
        }
    }

    @Nested
    class NetworkCallTests {

        @Test
        void successfulRealNetworkCallTest() {
            GetWarehouseBeerInfoRequest javaRequest = new GetWarehouseBeerInfoRequest();
            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder().build();
            GetWarehouseBeerInfoResponse javaResponse = new GetWarehouseBeerInfoResponse();

            when(mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(javaRequest)).thenReturn(grpcRequest);
            when(mapper.listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(any())).thenReturn(List.of(javaResponse));

            List<GetWarehouseBeerInfoResponse> result = grpcClientService.getWarehouseBeerInfo(javaRequest);

            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        void handlesRealGrpcErrorProperly() {
            GetWarehouseBeerInfoRequest javaRequest = new GetWarehouseBeerInfoRequest();

            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder()
                    .setSku("ERROR")
                    .build();

            when(mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(javaRequest)).thenReturn(grpcRequest);

            WarehouseServerException exception = assertThrows(WarehouseServerException.class, () -> {
                grpcClientService.getWarehouseBeerInfo(javaRequest);
            });

            assertTrue(exception.getMessage().contains("Warehouse is down"));
        }
    }
}
