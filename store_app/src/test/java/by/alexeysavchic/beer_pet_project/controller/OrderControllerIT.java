package by.alexeysavchic.beer_pet_project.controller;


import by.alexeysavchic.beer_pet_project.dto.request.CreateOrderRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderItemRequest;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderService;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class CreateOrderTests {
        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void successfulOrderRequest() throws Exception {
            OrderItemRequest orderItemRequest = new OrderItemRequest("validSku", 5);
            List<OrderItemRequest> list = List.of(orderItemRequest);
            CreateOrderRequest request = new CreateOrderRequest(list);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/order/create").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void invalidOrderRequest() throws Exception {
            OrderItemRequest orderItemRequest = new OrderItemRequest("", -5);
            List<OrderItemRequest> list = List.of(orderItemRequest);
            CreateOrderRequest request = new CreateOrderRequest(list);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/order/create").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$['cart[0].sku']", hasItem("sku must be between 1 and 30 symbols"))).
                    andExpect(jsonPath("$['cart[0].sku']", hasItem("must not be blank"))).
                    andExpect(jsonPath("$['cart[0].amount']", hasItem("must be greater than 0")));

        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void emptyCartRequest() throws Exception {
            List<OrderItemRequest> list = new ArrayList<>();
            CreateOrderRequest request = new CreateOrderRequest(list);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/order/create").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.cart", hasItem("must not be empty")));

        }

        @Test
        void notAuthenticatedRequest() throws Exception {
            OrderItemRequest orderItemRequest = new OrderItemRequest("validSku", 5);
            List<OrderItemRequest> list = List.of(orderItemRequest);
            CreateOrderRequest request = new CreateOrderRequest(list);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/order/create").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }
    }
}
