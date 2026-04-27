package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.Saga;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStage;
import by.alexeysavchic.beer_pet_project.entity.enums.SagaStatus;
import by.alexeysavchic.beer_pet_project.exception.UpdatingWarehouseXmlError;
import by.alexeysavchic.beer_pet_project.exception.UserNotFoundException;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.SagaRepository;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderOrchestrationService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderOrchestrationServiceImpl implements OrderOrchestrationService {
    private final OrderService orderService;

    private final WarehouseService warehouseService;

    private final EmailService emailService;

    private final SagaRepository sagaRepository;

    private final OrderRepository orderRepository;

    private final SecurityContextService securityContextService;

    private final UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    public void makeOrder(CartOrderRequest request) {
        LocalDateTime timeMark = LocalDateTime.now();
        ObjectNode rootNode = mapper.valueToTree(request);
        User user=securityContextService.getCurrentUser();
        Saga saga = new Saga();
        saga.setId(UUID.randomUUID());
        saga.setCreatedAt(timeMark);
        saga.setUpdatedAt(timeMark);
        saga.setPayload(rootNode);
        rootNode.put("userId", user.getId());
        saga.setStatus(SagaStatus.STARTED);
        saga.setStage(SagaStage.SAGA_CREATION);
        sagaRepository.save(saga);
        Order order = orderService.createOrder(request, timeMark, user);
        Long orderId = order.getId();
        rootNode.put("orderId", orderId);
        saga.setStage(SagaStage.SAVED_IN_DB);
        sagaRepository.save(saga);
        try {
            warehouseService.sendOrderToWarehouse(request, timeMark);
            saga.setStage(SagaStage.SENT_TO_WAREHOUSE);
            sagaRepository.save(saga);
        } catch (UpdatingWarehouseXmlError e) {
            orderService.cancelOrder(request, orderId);
        }
        emailService.sendEmail(order.getOrderItems(), order.getSummaryPrice(), user);
        saga.setStage(SagaStage.SENT_NOTIFICATION);
        saga.setStatus(SagaStatus.COMPLETED);
        sagaRepository.save(saga);
    }

    public void continueOrder(Saga saga)
    {
        ObjectNode rootNode=saga.getPayload();
        CartOrderRequest cart;
        try {
            cart=mapper.treeToValue(rootNode, CartOrderRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        LocalDateTime timeMark=LocalDateTime.now();
        saga.setUpdatedAt(timeMark);

        while (saga.getStatus()!=SagaStatus.COMPLETED)
        {
            switch (saga.getStage())
            {
                case SAGA_CREATION:
                    Long userId=rootNode.path("userId").asLong();
                    User user=userRepository.findUserById(userId).orElseThrow(()->new UserNotFoundException(userId));
                    Order order=orderService.createOrder(cart, timeMark, user);
                    rootNode.put("orderId", order.getId());
                    saga.setStage(SagaStage.SAVED_IN_DB);
                    sagaRepository.save(saga);
                case SAVED_IN_DB:
                    try
                    {
                        warehouseService.sendOrderToWarehouse(cart, timeMark);
                        saga.setStage(SagaStage.SENT_TO_WAREHOUSE);
                        sagaRepository.save(saga);
                    }
                    catch (UpdatingWarehouseXmlError e)
                    {
                        Long orderId = rootNode.path("orderId").asLong();
                        orderService.cancelOrder(cart, orderId);
                    }
                case SENT_TO_WAREHOUSE:
                {
                    Long orderId = rootNode.path("orderId").asLong();
                    Long userNotificationId= rootNode.path("userId").asLong();
                    Order orderNotification=orderRepository.findOrderById(orderId);
                    User userNotification=userRepository.findUserById(userNotificationId).
                            orElseThrow(()->new UserNotFoundException(userNotificationId));
                    emailService.sendEmail(orderNotification.getOrderItems(), orderNotification.getSummaryPrice(), userNotification);
                    saga.setStage(SagaStage.SENT_NOTIFICATION);
                    saga.setStatus(SagaStatus.COMPLETED);
                    sagaRepository.save(saga);
                }
            }
        }
    }

    public void cancelSaga(Saga saga)
    {
        ObjectNode rootNode=saga.getPayload();
        CartOrderRequest cart;
        try {
            cart=mapper.treeToValue(rootNode, CartOrderRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Long orderId = rootNode.path("orderId").asLong();
        LocalDateTime timeMark=LocalDateTime.now();
        while (saga.getStatus()!=SagaStatus.COMPLETED)
        {
            switch (saga.getStage())
            {
                case SAGA_CREATION:
                    sagaRepository.delete(saga);
                case SAVED_IN_DB:
                {
                    orderService.cancelOrder(cart, orderId);
                    sagaRepository.delete(saga);
                }
                case SENT_TO_WAREHOUSE:
                {
                    warehouseService.cancelOrderToWarehouse(cart, timeMark);
                    orderService.cancelOrder(cart, orderId);
                    sagaRepository.delete(saga);
                }
            }
        }
    }
}
