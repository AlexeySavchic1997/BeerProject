package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.dto.request.GetWarehouseBeerInfoRequest;
import by.alexeysavchic.beer_pet_project.entity.enums.ZoneType;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import by.alexeysavchic.beer_pet_project.service.Interface.WarehouseService;
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

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class WarehouseControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private WarehouseService warehouseService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class getBeerFromWarehouseTests
    {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulGetBeerFromWarehouse() throws Exception
        {
            GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest(1L, ZoneType.ZONE_SORTING, "1", 100, LocalDateTime.now());
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/warehouse").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesGetBeerFromWarehouse() throws Exception
        {
            GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest(1L, ZoneType.ZONE_SORTING, "1", 100, LocalDateTime.now());
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/warehouse").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void invalidGetBeerFromWarehouse() throws Exception
        {
            GetWarehouseBeerInfoRequest request = new GetWarehouseBeerInfoRequest(-1L, ZoneType.ZONE_SORTING, "", -100, LocalDateTime.now().plusDays(1));
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/warehouse").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.id", hasItem("Id can't be negative"))).
                    andExpect(jsonPath("$.sku", hasItem("sku must be between 1 and 30 symbols"))).
                    andExpect(jsonPath("$.amount", hasItem("Amount of items can't be negative"))).
                    andExpect(jsonPath("$.lastModifiedDate", hasItem("Date of modifications can't be in future")));
        }
    }
}
