package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.CartOrderRequest;
import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.WarehouseBeerInfo;
import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import by.alexeysavchic.beer_pet_project.exception.BeerIsAbsentInWarehouseException;
import by.alexeysavchic.beer_pet_project.exception.BeersNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.TransactionDidNotPassException;
import by.alexeysavchic.beer_pet_project.repository.OrderRepository;
import by.alexeysavchic.beer_pet_project.repository.WarehouseRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService
{
    private final OrderRepository orderRepository;

    private final WarehouseRepository warehouseRepository;

    private final SecurityContextService securityContextService;

    @Transactional
    @Override
    public Long createOrder(CartOrderRequest request, LocalDateTime timeMark)
    {
       Map<Long, Integer> cart=request.getCart();
       List<Long> keyList=new ArrayList<>(cart.keySet());
       List<WarehouseBeerInfo> warehouseList=warehouseRepository.findAllByBeerIdInSortingAndUnloadingZone(keyList);
       if (keyList.size()!=warehouseList.size())
       {
           List<Long> foundBeers=warehouseList.stream().map(info -> info.getBeer().getId()).toList();
           keyList.removeAll(foundBeers);
           throw new BeersNotFoundException(keyList);
       }
        Map<Long, WarehouseBeerInfo> sorting = new HashMap<>();
        Map<Long, WarehouseBeerInfo> unloading = new HashMap<>();
        for (WarehouseBeerInfo info : warehouseList) {
            if (info.getZoneType()==(ZoneType.ZONE_SORTING)) {
                sorting.put(info.getBeer().getId(), info);
            } else {
                unloading.put(info.getBeer().getId(), info);
            }
        }
       List<Long> beersWithNotEnoughAmount=new ArrayList<>();
        for (Long id:keyList)
        {
            if (sorting.get(id).getAmount()<cart.get(id))
            {
                beersWithNotEnoughAmount.add(id);
            }
        }
        if (!beersWithNotEnoughAmount.isEmpty())
        {
            throw new BeerIsAbsentInWarehouseException(beersWithNotEnoughAmount);
        }

       Order order = new Order();
       BigDecimal summaryPrice=BigDecimal.ZERO;
       for (Map.Entry<Long, Integer> entry:cart.entrySet())
       {
           Long id=entry.getKey();
           WarehouseBeerInfo sortingBeer = sorting.get(id);
           WarehouseBeerInfo unloadingBeer = unloading.get(id);
           Beer beer=sortingBeer.getBeer();
           Integer quantity=entry.getValue();
           BigDecimal price=beer.getPrice().multiply(new BigDecimal(quantity));
           OrderItem item=new OrderItem();
           item.setQuantity(quantity);
           item.setBeer(beer);
           item.setPrice(price);
           item.setOrder(order);
           summaryPrice=summaryPrice.add(price);
           order.getOrderItems().add(item);
           sortingBeer.setAmount(sortingBeer.getAmount() - quantity);
           unloadingBeer.setAmount(unloadingBeer.getAmount() + quantity);
       }
       order.setSummaryPrice(summaryPrice);
       order.setUser(securityContextService.getCurrentUser());
       order.setOrderDate(timeMark);
       orderRepository.save(order);
       warehouseRepository.saveAll(warehouseList);
       return order.getId();
    }

    @Transactional
    @Override
    public void cancelOrder(CartOrderRequest request, Long orderId)
    {
        orderRepository.delete(orderRepository.findOrderById(orderId));
        Map<Long, Integer> cart=request.getCart();
        List<Long> keyList=new ArrayList<>(cart.keySet());
        List<WarehouseBeerInfo> warehouseList=warehouseRepository.findAllByBeerIdInSortingAndUnloadingZone(keyList);
        Map<Long, WarehouseBeerInfo> sorting = new HashMap<>();
        Map<Long, WarehouseBeerInfo> unloading = new HashMap<>();
        for (WarehouseBeerInfo info : warehouseList) {
            if (info.getZoneType()==(ZoneType.ZONE_SORTING)) {
                sorting.put(info.getBeer().getId(), info);
            } else {
                unloading.put(info.getBeer().getId(), info);
            }
        }
        for (Map.Entry<Long, Integer> entry:cart.entrySet())
        {
            Long id=entry.getKey();
            Integer quantity=entry.getValue();
            WarehouseBeerInfo sortingBeer = sorting.get(id);
            WarehouseBeerInfo unloadingBeer = unloading.get(id);
            sortingBeer.setAmount(sortingBeer.getAmount() + quantity);
            unloadingBeer.setAmount(unloadingBeer.getAmount() - quantity);
        }
        warehouseRepository.saveAll(warehouseList);
    }








}
