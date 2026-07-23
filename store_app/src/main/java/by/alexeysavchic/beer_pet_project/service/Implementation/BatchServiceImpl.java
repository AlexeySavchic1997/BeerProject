package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.GenerateBatchRequest;
import by.alexeysavchic.beer_pet_project.entity.Batch;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderSet;
import by.alexeysavchic.beer_pet_project.entity.enums.BatchStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.exception.OrderSetNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.OrderSetWrongStatusException;
import by.alexeysavchic.beer_pet_project.repository.BatchRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.OrderSetRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;

    private final OrderRepository orderRepository;

    private final OrderSetRepository orderSetRepository;

    @Override
    public void generateBatch(GenerateBatchRequest request) {
        OrderSet orderSet = orderSetRepository.findById(request.getSetId()).orElseThrow(() -> new OrderSetNotFoundException());
        if (!orderSet.getOrderSetStatus().equals(OrderSetStatus.READY_TO_SPLIT))
        {
            throw new OrderSetWrongStatusException();
        }
        List<Order> orders = orderRepository.findAllByOrderSetId(orderSet.getId());
        Integer count= request.getCount();
        List<Batch>batches=new ArrayList<>();
        Integer batchAmount=orders.size()/count;
        Integer lastBatchSize=orders.size()%count;
        if (lastBatchSize>=10 || lastBatchSize==0)
        {
            regularSorting(orderSet, batchAmount, lastBatchSize, orders, count, batches);
        }
        else if (lastBatchSize<10 && request.getCount()<90)
        {
            lastBatchTooSmallSorting(orderSet, batchAmount, lastBatchSize, orders, count, batches);
        }
        else if (lastBatchSize<10 && request.getCount()>90)
        {
            LastBatchTooSmallPreviousTooLargeSorting(orderSet, batchAmount, lastBatchSize, orders, count, batches);
        }
        batches=batchRepository.saveAll(batches);
        for (Batch batch: batches)
        {
            batch.setBatchNumber("id: "+batch.getId()+ " date of creation/modification: "+batch.getCreatedOrLastModifiedDate()+ " count "+ batch.getCount());
        }
        batchRepository.saveAll(batches);
        orderRepository.saveAll(orders);
    }

    private void regularSorting(OrderSet orderSet, Integer batchAmount, Integer lastBatchSize, List<Order> orders, Integer count, List<Batch> batches)
    {
        for (int i=0;i<batchAmount;i++)
        {
            Batch batch=new Batch();
            batch.setStatus(BatchStatus.NEW);
            batch.setCount(count);
            batch.setOrderSet(orderSet);
            batch.setCreatedOrLastModifiedDate(LocalDateTime.now());
            List<Order> batchOrders=orders.subList(i*count, (i+1)*count);
            for (Order order:batchOrders)
            {
                order.setBatch(batch);
            }
            batches.add(batch);
            if (lastBatchSize==0)
            {
                return;
            }
        }
        List<Order> batchOrders=orders.subList(orders.size()-lastBatchSize, orders.size());
        Batch batch=new Batch();
        batch.setStatus(BatchStatus.NEW);
        batch.setCount(batchOrders.size());
        batch.setOrderSet(orderSet);
        batch.setCreatedOrLastModifiedDate(LocalDateTime.now());
        for (Order order:batchOrders)
        {
            order.setBatch(batch);
        }
        batches.add(batch);
    }

    private void LastBatchTooSmallPreviousTooLargeSorting(OrderSet orderSet, Integer batchAmount, Integer lastBatchSize, List<Order> orders, Integer count, List<Batch> batches)
    {
        for (int i=0;i<batchAmount-1;i++)
        {
            Batch batch=new Batch();
            batch.setStatus(BatchStatus.NEW);
            batch.setCount(count);
            batch.setOrderSet(orderSet);
            batch.setCreatedOrLastModifiedDate(LocalDateTime.now());
            List<Order> batchOrders=orders.subList(i*count, (i+1)*count);
            for (Order order:batchOrders)
            {
                order.setBatch(batch);
            }
            batches.add(batch);
        }
        if (lastBatchSize==0)
        {
            return;
        }
        List<Order> batchOrders1=orders.subList(orders.size()-(lastBatchSize+count), orders.size()-(lastBatchSize+count)/2);
        Batch batch1=new Batch();
        batch1.setStatus(BatchStatus.NEW);
        batch1.setCount(batchOrders1.size());
        batch1.setOrderSet(orderSet);
        batch1.setCreatedOrLastModifiedDate(LocalDateTime.now());
        for (Order order:batchOrders1)
        {
            order.setBatch(batch1);
        }
        batches.add(batch1);
        List<Order> batchOrders2=orders.subList(orders.size()-(lastBatchSize+count)/2, orders.size());
        Batch batch2=new Batch();
        batch2.setStatus(BatchStatus.NEW);
        batch2.setCount(batchOrders2.size());
        batch2.setOrderSet(orderSet);
        batch2.setCreatedOrLastModifiedDate(LocalDateTime.now());
        for (Order order:batchOrders2)
        {
            order.setBatch(batch2);
        }
        batches.add(batch2);
    }

    private void lastBatchTooSmallSorting(OrderSet orderSet, Integer batchAmount, Integer lastBatchSize, List<Order> orders, Integer count, List<Batch> batches)
    {
        for (int i=0;i<batchAmount-1;i++)
        {
            Batch batch=new Batch();
            batch.setStatus(BatchStatus.NEW);
            batch.setCount(count);
            batch.setOrderSet(orderSet);
            batch.setCreatedOrLastModifiedDate(LocalDateTime.now());
            List<Order> batchOrders=orders.subList(i*count, (i+1)*count);
            for (Order order:batchOrders)
            {
                order.setBatch(batch);
            }
            batches.add(batch);
            if (lastBatchSize==0)
            {
                return;
            }
        }
        List<Order> batchOrders=orders.subList(orders.size()-lastBatchSize+count, orders.size());
        Batch batch=new Batch();
        batch.setStatus(BatchStatus.NEW);
        batch.setCount(batchOrders.size());
        batch.setOrderSet(orderSet);
        batch.setCreatedOrLastModifiedDate(LocalDateTime.now());
        for (Order order:batchOrders)
        {
            order.setBatch(batch);
        }
        batches.add(batch);
    }


}
