package by.alexeysavchic.service.implementation;

import by.alexeysavchic.mapper.BeerGRPCMapper;
import by.alexeysavchic.service.interaface.XMLParserService;
import com.google.rpc.Status;
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

    @Override
    public void updateWarehouseInfo(UpdateBeerRequest request, StreamObserver<Status> responseObserver)
    {
        try {
            xmlParserService.setWarehouseInfo(beerMapper.GrpcRequestToXml(request));
        }
        catch (RuntimeException e)
        {
            Status status = Status. newBuilder().
                    setMessage(e.getMessage()).
                    setCode(io.grpc.Status.Code.INTERNAL.value()).
                    build();
            responseObserver.onNext(status);
            responseObserver.onCompleted();
        }

        Status status = Status. newBuilder().
                setCode(io.grpc.Status.Code.OK.value()).
                build();
        responseObserver.onNext(status);
        responseObserver.onCompleted();
    }
}
