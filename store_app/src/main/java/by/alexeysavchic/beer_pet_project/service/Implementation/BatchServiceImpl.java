package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.GenerateBatchRequest;
import by.alexeysavchic.beer_pet_project.entity.Batch;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.enums.BatchStatus;
import by.alexeysavchic.beer_pet_project.exception.OrderSetNotFoundException;
import by.alexeysavchic.beer_pet_project.repository.BatchRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderSetRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    private final OrderRepository orderRepository;

    private final OrderSetRepository orderSetRepository;

    @Override
    public void generateBatch(GenerateBatchRequest request) {
        Long setId = request.getSetId();
        Batch batch = new Batch();
        batch.setCount(request.getCount());
        batch.setOrderSet(orderSetRepository.findById(setId).orElseThrow(() -> new OrderSetNotFoundException()));
        List<Order> orders = orderRepository.findAllByOrderSetId(setId, Limit.of(request.getCount()));
        batch.setOrders(orders);
        for (Order order : orders) {
            order.setBatch(batch);
        }
        batch.setStatus(BatchStatus.NEW);
        batchRepository.save(batch);
        orderRepository.saveAll(orders);
    }
}
