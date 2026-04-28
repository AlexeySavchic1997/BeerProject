package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UpdatePacketRequest;
import warehouse_api.UpdatePacketResponse;
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
    public void updateWarehouseInfo(UpdatePacketRequest packet, StreamObserver<UpdatePacketResponse> responseObserver) {
        try {
            xmlParserService.setWarehouseInfo(beerMapper.updatePacketToListUpdateWarehouseDTO(packet.getRequestList()));
        } catch (RuntimeException e) {
            UpdatePacketResponse updateResponse = UpdatePacketResponse.newBuilder().setSuccess(false).
                    setMessage(e.getMessage()).build();
            responseObserver.onNext(updateResponse);
            responseObserver.onCompleted();
            return;
        }
        UpdatePacketResponse updateResponse = UpdatePacketResponse.newBuilder().setSuccess(true).
                setMessage("success").build();
        responseObserver.onNext(updateResponse);
        responseObserver.onCompleted();
    }
}
