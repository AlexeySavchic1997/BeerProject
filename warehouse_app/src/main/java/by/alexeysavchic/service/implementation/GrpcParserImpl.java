package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoResponse;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.WarehouseApiGrpc;

@GrpcService
@RequiredArgsConstructor
public class GrpcParserImpl extends WarehouseApiGrpc.WarehouseApiImplBase {
    private final BeerGRPCMapper mapper;

    private final XMLParserService xmlParserService;

    @Override
    public void getUpdatedWarehouseInfo(Timestamp request, StreamObserver<BeerInfoResponse> responseObserver) {
        request.
        BeerInfoResponse response = mapper.XmlDtoToGrpcDto(xmlParserService.getActualWarehouseInfo(request));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getWarehouseInfo(Empty request, StreamObserver<BeerInfoResponse> responseObserver) {
        BeerInfoResponse response = mapper.XmlDtoToGrpcDto(xmlParserService.getFilteredWarehouseInfo());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateWarehouseInfo(UpdateBeerRequest request, StreamObserver<Empty> responseObserver) {
        xmlParserService.setWarehouseInfo(mapper.GrpcRequestToXml(request.getBeerList()));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
