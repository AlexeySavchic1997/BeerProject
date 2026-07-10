package by.alexeysavchic.service;

import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import by.alexeysavchic.dto.ZoneType;
import by.alexeysavchic.service.implementation.GrpcParserImpl;
import by.alexeysavchic.service.interaface.XMLParserService;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.WarehouseApiGrpc;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GrpcParserIT {

    @Autowired
    private GrpcParserImpl grpcParser;

    @MockitoBean
    private XMLParserService xmlParserService;

    private Server inProcessServer;
    private ManagedChannel inProcessChannel;

    private WarehouseApiGrpc.WarehouseApiBlockingStub blockingStub;

    @BeforeEach
    void setUp() throws IOException {
        String serverName = InProcessServerBuilder.generateName();

        inProcessServer = InProcessServerBuilder
                .forName(serverName)
                .directExecutor()
                .addService(grpcParser)
                .build()
                .start();

        inProcessChannel = InProcessChannelBuilder
                .forName(serverName)
                .directExecutor()
                .build();

        blockingStub = WarehouseApiGrpc.newBlockingStub(inProcessChannel);
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
    class getWarehouseInfoTests {

        @Test
        void successfullyRetrievesWarehouseInfo() {
            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder()
                    .setSku("BEER-TEST-1")
                    .build();

            WarehouseXmlInfoDTO mockXmlResponse = new WarehouseXmlInfoDTO();
            mockXmlResponse.setId(10L);
            mockXmlResponse.setSku("BEER-TEST-1");
            mockXmlResponse.setAmount(150);
            mockXmlResponse.setZoneType(ZoneType.ZONE_SORTING);
            mockXmlResponse.setLastModifiedDate(LocalDateTime.now());

            when(xmlParserService.getWarehouseInfo(any(InputConditionDTO.class)))
                    .thenReturn(List.of(mockXmlResponse));

            BeerInfoResponse response = blockingStub.getWarehouseInfo(grpcRequest);

            assertNotNull(response);
            assertEquals(1, response.getBeerCount());

            assertEquals(10L, response.getBeer(0).getId());
            assertEquals("BEER-TEST-1", response.getBeer(0).getSku());
            assertEquals(150, response.getBeer(0).getAmount());

            verify(xmlParserService).getWarehouseInfo(any(InputConditionDTO.class));
        }
    }
}
