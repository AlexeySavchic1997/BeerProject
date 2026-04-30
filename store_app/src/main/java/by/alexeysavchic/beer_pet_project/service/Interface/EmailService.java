package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface EmailService {
    public void confirmOrderEmail(List<OrderItem> orders, BigDecimal price, User user);

    public void insufficientInventoryOrderEmail(Map<String, Integer> unpassedOrdersMap, User user);
}
