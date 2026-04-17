package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UpdateWarehouseInfoDTO;
import by.alexeysavchic.beer_pet_project.dto.response.GetWarehouseBeerInfoResponse;
import by.alexeysavchic.beer_pet_project.exception.WarehouseServerException;
import by.alexeysavchic.beer_pet_project.exception.WarehouseUpdateServerException;
import by.alexeysavchic.beer_pet_project.mapper.GrpcMapper;
import by.alexeysavchic.beer_pet_project.service.Interface.ClientService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import warehouse_api.BeerInfoResponse;
import warehouse_api.UpdateBeerRequest;
import warehouse_api.UpdatePacket;
import warehouse_api.UpdateResponse;
import warehouse_api.WarehouseApiGrpc;

import java.util.List;

@ConfigurationProperties(prefix = "by.group.project")
@RequiredArgsConstructor
@Setter
@Getter
@GrpcService
public class GrpcClientService implements ClientService {
    private WarehouseApiGrpc.WarehouseApiBlockingStub blockingStub;

    private String warehouseHost;

    private int warehousePort;

    private final GrpcMapper mapper;

    @PostConstruct
    public void init() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(warehouseHost, warehousePort)
                .usePlaintext()
                .build();
        this.blockingStub = WarehouseApiGrpc.newBlockingStub(channel);
    }

    @Override
    public List<GetWarehouseBeerInfoResponse> getWarehouseBeerInfo(GetWarehouseBeerInfoRequest request) {
        try {
            BeerInfoResponse warehouseInfo = blockingStub.
                    getWarehouseInfo(mapper.getWarehouseBeerInfoRequestToGetWarehouseInfoRequest(request));
            return mapper.listWarehouseBeerInfoToListGetWarehouseBeerInfoResponse(warehouseInfo.getBeerList());
        } catch (StatusRuntimeException e) {
            throw new WarehouseServerException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void updateWarehouseInfo(List<UpdateWarehouseInfoDTO> updates) {

        List<UpdateBeerRequest> updateList=mapper.listUpdateWarehouseInfoDTOToListUpdateBeerRequest(updates);

        UpdatePacket packet= UpdatePacket.newBuilder().addAllRequest(updateList).build();
        UpdateResponse updateResponse = blockingStub.updateWarehouseInfo(packet);

        if (!updateResponse.getSuccess() && updateResponse.getMessage().equals("There is no required amount items in warehouse")) {
            throw new WarehouseUpdateServerException(updateResponse.getMessage());
        }
        if (!updateResponse.getSuccess()) {
            throw new WarehouseUpdateServerException(updateResponse.getMessage());
        }

    }
}
