package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerRequest;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerService;
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

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class BeerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private BeerService beerService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class AddNewBeerTests {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulAddBeerRequest() throws Exception {
            AddBeerRequest request = new AddBeerRequest("validSku", "validName", "description", BigDecimal.ONE, BigDecimal.TEN, "beerBrand", new ArrayList<>());
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/add").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesBeerRequest() throws Exception {
            AddBeerRequest request = new AddBeerRequest("validSku", "validName", "description", BigDecimal.ONE, BigDecimal.TEN, "beerBrand", new ArrayList<>());
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/add").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void invalidAddBeerBrandRequest() throws Exception {
            AddBeerRequest request = new AddBeerRequest("", "", "description", BigDecimal.ONE.negate(), BigDecimal.TEN.negate(), "beerBrand", new ArrayList<>());
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/add").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.sku", hasItem("sku must be between 1 and 30 symbols"))).
                    andExpect(jsonPath("$.sku", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.name", hasItem("beer name must be between 2 and 20 symbols"))).
                    andExpect(jsonPath("$.name", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.volume", hasItem("volume must be positive"))).
                    andExpect(jsonPath("$.price", hasItem("price must be positive")));
        }
    }

    @Nested
    class DeleteBeerTests {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulDeleteBeerBrandRequest() throws Exception {

            mockMvc.perform(delete("/api/v1/beer/beerSKU")).
                    andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesDeleteBeerBrandRequest() throws Exception {

            mockMvc.perform(delete("/api/v1/beer/beerSKU")).
                    andExpect(status().isForbidden());
        }
    }
}
