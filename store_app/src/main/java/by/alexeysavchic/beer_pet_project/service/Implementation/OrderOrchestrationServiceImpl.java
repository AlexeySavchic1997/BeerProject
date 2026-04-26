package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.Saga;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStage;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStatus;
import by.alexeysavchic.beer_pet_project.exception.UpdatingWarehouseXmlError;
import by.alexeysavchic.beer_pet_project.repository.SagaRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
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

    private final EmailService emailService;

    private final SagaRepository sagaRepository;

    private ObjectMapper mapper = new ObjectMapper();

    public void makeOrder(CartOrderRequest request)
    {
        LocalDateTime timeMark=LocalDateTime.now();
        ObjectNode rootNode=mapper.createObjectNode();
        ObjectNode cartNode=mapper.createObjectNode();
        Map<Long, Integer> cartMap=request.getCart();
        for (Map.Entry<Long, Integer> entry:cartMap.entrySet())
        {
            cartNode.put(String.valueOf(entry.getKey()), entry.getValue());
        }
        rootNode.set("cart", cartNode);
        Saga saga=new Saga();
        saga.setStatus(SagaStatus.STARTED);
        saga.setStage(SagaStage.SAGA_CREATION);
        saga.setId(UUID.randomUUID());
        saga.setCreatedAt(timeMark);
        sagaRepository.save(saga);
        Order order=orderService.createOrder(request, timeMark);
        Long orderId=order.getId();
        rootNode.put("orderId", orderId);
        saga.setStage(SagaStage.SAVED_IN_DB);
        sagaRepository.save(saga);
        try
        {
        warehouseService.sendOrderToWarehouse(request, timeMark);
        }
        catch (UpdatingWarehouseXmlError e)
        {
            orderService.cancelOrder(request, orderId);
        }
        saga.setStage(SagaStage.SENT_TO_WAREHOUSE);
        sagaRepository.save(saga);
        emailService.sendEmail(order.getOrderItems(), order.getSummaryPrice());
        saga.setStage(SagaStage.SENT_NOTIFICATION);
        saga.setStatus(SagaStatus.COMPLETED);
        sagaRepository.save(saga);
    }

    public void continueOrder(Saga saga)
    {

    }

    public void cancelSaga(Saga saga)
    {

    }
}
