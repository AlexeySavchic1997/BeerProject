package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.entity.Beer;
import by.alexeysavchic.beer_pet_project.entity.OrderItem;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.service.Implementation.EmailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> messageCaptor;

    private final String STORE_EMAIL = "store@beershop.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "storeEmail", STORE_EMAIL);
    }

    @Nested
    class confirmOrderEmailTests {

        @Test
        void successfulConfirmOrderEmailTest() {
            User user = new User();
            user.setEmail("client@gmail.com");

            Beer beer = new Beer();
            beer.setName("Guinness");

            OrderItem orderItem = new OrderItem();
            orderItem.setBeer(beer);
            orderItem.setQuantity(3);

            BigDecimal price = new BigDecimal("15.50");

            emailService.confirmOrderEmail(List.of(orderItem), price, user);

            verify(mailSender, times(1)).send(messageCaptor.capture());

            SimpleMailMessage sentMessage = messageCaptor.getValue();

            assertNotNull(sentMessage);
            assertEquals(STORE_EMAIL, sentMessage.getFrom());
            assertNotNull(sentMessage.getTo());
            assertEquals("client@gmail.com", sentMessage.getTo()[0]);
            assertEquals("Order from beer shop", sentMessage.getSubject());

            String text = sentMessage.getText();
            assertNotNull(text);
            assertTrue(text.contains("Guinness"));
            assertTrue(text.contains("3"));
            assertTrue(text.contains("15.50"));
        }
    }

    @Nested
    class insufficientInventoryOrderEmailTests {

        @Test
        void successfulInsufficientInventoryOrderEmailTest() {
            User user = new User();
            user.setEmail("client@gmail.com");

            Map<String, Integer> unpassedOrders = Map.of("Heineken", 5);

            emailService.insufficientInventoryOrderEmail(unpassedOrders, user);

            verify(mailSender, times(1)).send(messageCaptor.capture());
            SimpleMailMessage sentMessage = messageCaptor.getValue();

            assertNotNull(sentMessage);
            assertEquals(STORE_EMAIL, sentMessage.getFrom());
            assertEquals("client@gmail.com", sentMessage.getTo()[0]);
            assertEquals("Order from beer shop", sentMessage.getSubject());

            String text = sentMessage.getText();
            assertNotNull(text);
            assertTrue(text.contains("Heineken"));
            assertTrue(text.contains("5"));
        }
    }

    @Nested
    class insufficientInventorySubscriptionEmailTests {

        @Test
        void successfulInsufficientInventorySubscriptionEmailTest() {
            User user = new User();
            user.setEmail("client@gmail.com");

            List<String> unpassedBeers = List.of("Paulaner", "Corona");

            emailService.insufficientInventorySubscriptionEmail(unpassedBeers, user);

            verify(mailSender, times(1)).send(messageCaptor.capture());
            SimpleMailMessage sentMessage = messageCaptor.getValue();

            assertNotNull(sentMessage);
            assertEquals(STORE_EMAIL, sentMessage.getFrom());
            assertEquals("client@gmail.com", sentMessage.getTo()[0]);
            assertEquals("Order from beer shop", sentMessage.getSubject());

            String text = sentMessage.getText();
            assertNotNull(text);
            assertTrue(text.contains("Paulaner"));
            assertTrue(text.contains("Corona"));
        }
    }
}