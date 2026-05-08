package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.service.Implementation.messages.EmailMessages;
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
        message.setText(String.format(EmailMessages.successfulOrder, cart, price));
        mailSender.send(message);
    }

    @Override
    public void insufficientInventoryOrderEmail(Map<String, Integer> unpassedOrdersMap, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(storeEmail);
        message.setTo(user.getEmail());
        message.setSubject("Order from beer shop");
        message.setText(String.format(EmailMessages.insufficientInventory, unpassedOrdersMap));
        mailSender.send(message);
    }

    @Override
    public void insufficientInventorySubscriptionEmail(List<String> unpassedBeerNames, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(storeEmail);
        message.setTo(user.getEmail());
        message.setSubject("Order from beer shop");
        message.setText(String.format(EmailMessages.insufficientInventory, unpassedBeerNames));
        mailSender.send(message);
    }


}
