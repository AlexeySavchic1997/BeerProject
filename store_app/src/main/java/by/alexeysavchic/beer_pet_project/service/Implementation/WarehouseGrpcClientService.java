package by.alexeysavchic.beer_pet_project.service.Implementation;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import warehouse_api.WarehouseApiGrpc;

@Service
public class WarehouseGrpcClientService
{
    private final WarehouseApiGrpc.WarehouseApiBlockingStub blockingStub;

    public WarehouseGrpcClientService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.blockingStub = WarehouseApiGrpc.newBlockingStub(channel);
    }

}
