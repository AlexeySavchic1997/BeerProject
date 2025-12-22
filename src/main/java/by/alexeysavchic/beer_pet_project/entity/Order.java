package by.alexeysavchic.beer_pet_project.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Order
{
    Integer id;
    LocalDateTime orderTime;
    Integer summaryPrice;
    User user;
    List<OrderItem> orderItems;
}
