package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.GetOrderSetsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.OrderSetSplitRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetOrderSetResponse;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderSetStatus;
import by.alexeysavchic.beer_pet_project.entity.enums.OrderType;
import by.alexeysavchic.beer_pet_project.entity.enums.SplitType;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.OrderSetService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderSetController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class OrderSetControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private OrderSetService orderSetService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class getAllOrderSetsTests {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulGetAllOrderSetsRequest() throws Exception {

            GetOrderSetsRequest request = new GetOrderSetsRequest(OrderSetStatus.WAITING_FOR_SPLIT, SplitType.GENDER, OrderType.FAVORITE_BEER);
            String jsonBody = objectMapper.writeValueAsString(request);
            GetOrderSetResponse response = new GetOrderSetResponse();
            response.setId(1L);
            response.setStatus(OrderSetStatus.WAITING_FOR_SPLIT);
            response.setCommonQuantity(100);
            List<GetOrderSetResponse> responseList = new ArrayList<>();
            responseList.add(response);
            when(orderSetService.getOrderSets(any(GetOrderSetsRequest.class))).thenReturn(responseList);

            mockMvc.perform(post("/api/v1/set/get").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$[0].id").value(1L)).
                    andExpect(jsonPath("$[0].commonQuantity").value(100)).
                    andExpect(jsonPath("$[0].status").value("WAITING_FOR_SPLIT"));
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesGetAllOrderSetsRequest() throws Exception {

            GetOrderSetsRequest request = new GetOrderSetsRequest(OrderSetStatus.WAITING_FOR_SPLIT, SplitType.GENDER, OrderType.FAVORITE_BEER);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/set/get").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }

        @Nested
        class MarkSplitTests {
            @Test
            @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
            void successfulMarkSplitRequest() throws Exception {
                OrderSetSplitRequest request = new OrderSetSplitRequest(List.of(1L, 2L, 3L), SplitType.GENDER);
                String jsonBody = objectMapper.writeValueAsString(request);

                mockMvc.perform(post("/api/v1/set/split").
                                contentType(MediaType.APPLICATION_JSON_VALUE).
                                content(jsonBody)).
                        andExpect(status().isOk());
            }

            @Test
            @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
            void insufficientPrivilegesMarkSplitRequest() throws Exception {
                OrderSetSplitRequest request = new OrderSetSplitRequest(List.of(1L, 2L, 3L), SplitType.GENDER);
                String jsonBody = objectMapper.writeValueAsString(request);

                mockMvc.perform(post("/api/v1/set/split").
                                contentType(MediaType.APPLICATION_JSON_VALUE).
                                content(jsonBody)).
                        andExpect(status().isForbidden());
            }

            @Test
            @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
            void invalidMarkSplitRequest() throws Exception {
                List<Long> ids = new ArrayList<>();
                OrderSetSplitRequest request = new OrderSetSplitRequest(ids, SplitType.GENDER);
                String jsonBody = objectMapper.writeValueAsString(request);

                mockMvc.perform(post("/api/v1/set/split").
                                contentType(MediaType.APPLICATION_JSON_VALUE).
                                content(jsonBody)).
                        andExpect(status().isBadRequest()).
                        andExpect(jsonPath("$.ids", hasItem("must not be empty")));

            }
        }
    }
}
