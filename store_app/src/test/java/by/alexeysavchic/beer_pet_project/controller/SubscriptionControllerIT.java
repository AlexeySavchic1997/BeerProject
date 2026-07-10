package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.CreateSubscriptionRequest;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.TypeOfSubscription;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
import by.alexeysavchic.beer_pet_project.service.Interface.SubscriptionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class SubscriptionControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class CreateSubscriptionTests {
        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void successfulCreateSubscription() throws Exception {

            List<String> stringList = List.of("1", "2", "3");
            CreateSubscriptionRequest request = new CreateSubscriptionRequest(TypeOfSubscription.FAVORITE_BEER, 3, stringList);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/subscription").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void invalidCreateSubscription() throws Exception {

            List<String> stringList = new ArrayList<>();
            CreateSubscriptionRequest request = new CreateSubscriptionRequest(TypeOfSubscription.FAVORITE_BEER, 13, stringList);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/subscription").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.durationMonths").value("Duration should not be greater than 12")).
                    andExpect(jsonPath("$.skuList").value("must not be empty"));
        }

    }

    @Nested
    class ProcessSubscriptions {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulProcessSubscriptions() throws Exception {

            OrderType request = OrderType.BEER_OF_THE_MONTH;
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(patch("/api/v1/subscription").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientProcessSubscriptions() throws Exception {

            OrderType request = OrderType.BEER_OF_THE_MONTH;
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(patch("/api/v1/subscription").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }
    }
}
