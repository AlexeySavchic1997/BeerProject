package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String storeEmail;

    @Override
    public void confirmOrderEmail(List<OrderItem> orders, BigDecimal price, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(storeEmail);
        message.setTo(user.getEmail());
        message.setSubject("Order from beer shop");
        Map<String, Integer> cart = new HashMap<>();
        for (OrderItem item : orders) {
            cart.put(item.getBeer().getName(), item.getQuantity());
        }
        message.setText("Your order " + cart + " with price " + price + "confirmed ");
        mailSender.send(message);
    }

    public void insufficientInventoryOrderEmail(Map<String, Integer> unpassedOrdersMap, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(storeEmail);
        message.setTo(user.getEmail());
        message.setSubject("Order from beer shop");
        message.setText("Our apologise but we have not this beers now " + unpassedOrdersMap + " We will contact you shortly");
        mailSender.send(message);
    }
}
