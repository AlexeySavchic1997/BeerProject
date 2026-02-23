package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import warehouse_api.BeerInfoList;
import warehouse_api.BeerUpdateList;
import warehouse_api.WarehouseApiGrpc;

@GrpcService
@RequiredArgsConstructor
public class GrpcParserImpl extends WarehouseApiGrpc.WarehouseApiImplBase
{
    private final BeerGRPCMapper mapper;

    private final XMLParserService xmlParserService;

    @Override
    public void getWarehouseInfo(Empty request, StreamObserver<BeerInfoList> responseObserver) {
        BeerInfoList response=mapper.XmlDtoToGrpcDto(xmlParserService.getWarehouseInfo());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateWarehouseInfo(BeerUpdateList request, StreamObserver<Empty> responseObserver)
    {
        xmlParserService.setWarehouseInfo(mapper.GrpcRequestToXml(request));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
