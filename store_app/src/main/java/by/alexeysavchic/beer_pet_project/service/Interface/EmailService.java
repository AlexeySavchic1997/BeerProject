package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface EmailService
{
    public void sendEmail(List<OrderItem> orders, BigDecimal price, User user);
}
