package by.alexeysavchic.beer_pet_project.service.Implementation;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import warehouse_api.WarehouseApiGrpc;

@Service
@ConfigurationProperties(prefix = "by.group.project")
@NoArgsConstructor
@Setter
@Getter
public class WarehouseGrpcClientService {
    private WarehouseApiGrpc.WarehouseApiBlockingStub blockingStub;

    private String warehouseHost;

    private int warehousePort;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(warehouseHost, warehousePort)
                .usePlaintext()
                .build();
        this.blockingStub = WarehouseApiGrpc.newBlockingStub(channel);
    }

}
