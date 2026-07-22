package by.alexeysavchic.service;

import by.alexeysavchic.dto.InputConditionDTO;
import by.alexeysavchic.dto.WarehouseXmlInfoDTO;
import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.implementation.GrpcParserImpl;
import by.alexeysavchic.service.interaface.XMLParserService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GrpcParserTest {

    @Mock
    private BeerGRPCMapper beerMapper;

    @Mock
    private XMLParserService xmlParserService;

    @Mock
    private StreamObserver<BeerInfoResponse> responseObserver;

    @InjectMocks
    private GrpcParserImpl grpcParser;

    @Nested
    class getWarehouseInfoTests {

        @Test
        void successfulGetWarehouseInfoTest() {
            GetWarehouseInfoRequest grpcRequest = GetWarehouseInfoRequest.newBuilder().build();
            InputConditionDTO inputCondition = new InputConditionDTO();
            List<WarehouseXmlInfoDTO> xmlInfoList = List.of(new WarehouseXmlInfoDTO());
            BeerInfoResponse expectedGrpcResponse = BeerInfoResponse.newBuilder().build();

            when(beerMapper.beerRequestToInputCondition(grpcRequest)).thenReturn(inputCondition);
            when(xmlParserService.getWarehouseInfo(inputCondition)).thenReturn(xmlInfoList);
            when(beerMapper.XmlDtoToGrpcDto(xmlInfoList)).thenReturn(expectedGrpcResponse);

            grpcParser.getWarehouseInfo(grpcRequest, responseObserver);

            verify(beerMapper, times(1)).beerRequestToInputCondition(grpcRequest);
            verify(xmlParserService, times(1)).getWarehouseInfo(inputCondition);
            verify(beerMapper, times(1)).XmlDtoToGrpcDto(xmlInfoList);

            verify(responseObserver, times(1)).onNext(expectedGrpcResponse);
            verify(responseObserver, times(1)).onCompleted();
            verify(responseObserver, never()).onError(any());
        }
    }
}