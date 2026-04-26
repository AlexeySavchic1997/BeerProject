package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.entity.Order;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService
{
    private final JavaMailSender mailSender;

    private final SecurityContextService securityContextService;

    public void sendEmail(List<OrderItem> orders, BigDecimal price)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("beer_store@gmail.com");
        message.setTo(securityContextService.getCurrentUser().getEmail());
        message.setSubject("Order from beer shop");
        Map<String, Integer> cart=new HashMap<>();
        for (OrderItem item:orders)
        {
            cart.put(item.getBeer().getName(), item.getQuantity());
        }
        message.setText("Your order "+cart+" with price " + price + "confirmed ");
        mailSender.send(message);
    }
}
