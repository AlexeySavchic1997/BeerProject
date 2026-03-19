package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoResponse;
import warehouse_api.GetWarehouseInfoRequest;
import warehouse_api.UpdateBeerRequest;
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

    //    as I read Empty is a body of response. We'll get GRPC status even with StreamObserver<Empty>
    @Override
    public void updateWarehouseInfo(UpdateBeerRequest request, StreamObserver<Empty> responseObserver) {
        xmlParserService.setWarehouseInfo(beerMapper.GrpcRequestToXml(request));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
