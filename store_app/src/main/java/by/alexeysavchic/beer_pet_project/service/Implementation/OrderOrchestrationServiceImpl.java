package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Saga;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStage;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStatus;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderOrchestrationService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOrchestrationServiceImpl implements OrderOrchestrationService
{
    private final OrderService orderService;

    private final WarehouseService warehouseService;

    ObjectMapper mapper = new ObjectMapper();

    public void makeOrder(CartOrderRequest request)
    {
        ObjectNode rootNode=mapper.createObjectNode();
        ObjectNode cartNode=mapper.createObjectNode();
        Map<Long, Integer> cartMap=request.getCart();
        for (Map.Entry<Long, Integer> entry:cartMap.entrySet())
        {
            cartNode.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        rootNode.set("cart", cartNode);
        LocalDateTime timeMark=LocalDateTime.now();
        Saga saga=new Saga();
        saga.setStatus(SagaStatus.STARTED);
        saga.setId(UUID.randomUUID());
        saga.setCreatedAt(timeMark);
        saga.setStage(SagaStage.SAVING_IN_DB);


    }
}
