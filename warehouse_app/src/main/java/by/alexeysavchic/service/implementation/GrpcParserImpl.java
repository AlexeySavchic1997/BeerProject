package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.mapper.TimeGrpcJavaMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoResponse;
import warehouse_api.BeerRequest;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.WarehouseApiGrpc;

@GrpcService
@RequiredArgsConstructor
public class GrpcParserImpl extends WarehouseApiGrpc.WarehouseApiImplBase {
    private final BeerGRPCMapper beerMapper;

    private final TimeGrpcJavaMapper timeMapper;

    private final XMLParserService xmlParserService;

    @Override
    public void getUpdatedWarehouseInfo(Timestamp request, StreamObserver<BeerInfoResponse> responseObserver) {
        BeerInfoResponse response = beerMapper.XmlDtoToGrpcDto(xmlParserService.getActualWarehouseInfo(timeMapper.timestampToLocalDateTime(request)));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getWarehouseInfo(BeerRequest request, StreamObserver<BeerInfoResponse> responseObserver) {
        BeerInfoResponse response = beerMapper.XmlDtoToGrpcDto(xmlParserService.getFilteredWarehouseInfo(beerMapper.beerRequestToInputCondition(request)));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateWarehouseInfo(UpdateBeerRequest request, StreamObserver<Empty> responseObserver) {
        xmlParserService.setWarehouseInfo(beerMapper.GrpcRequestToXml(request.getBeerList()));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
