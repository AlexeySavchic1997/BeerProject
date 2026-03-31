package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.UpdateResponse;
import warehouse_api.WarehouseApiGrpc;

@GrpcService
@RequiredArgsConstructor
public class GrpcParserImpl extends WarehouseApiGrpc.WarehouseApiImplBase {

    private final BeerGRPCMapper beerMapper;

    private final XMLParserService xmlParserService;

    @Override
    public void getWarehouseInfo(GetWarehouseInfoRequest request, StreamObserver<BeerInfoResponse> responseObserver) {
        BeerInfoResponse response = beerMapper.XmlDtoToGrpcDto(xmlParserService.getWarehouseInfo(beerMapper.beerRequestToInputCondition(request)));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateWarehouseInfo(UpdateBeerRequest request, StreamObserver<UpdateResponse> responseObserver) {
        try {
            xmlParserService.setWarehouseInfo(beerMapper.GrpcRequestToXml(request));
        } catch (RuntimeException e) {
            UpdateResponse updateResponse = UpdateResponse.newBuilder().setSuccess(false).
                    setMess(e.getMessage()).build();
            responseObserver.onNext(updateResponse);
            responseObserver.onCompleted();
        }
        UpdateResponse updateResponse = UpdateResponse.newBuilder().setSuccess(true).
                setMess("success").build();
        responseObserver.onNext(updateResponse);
        responseObserver.onCompleted();
    }
}
