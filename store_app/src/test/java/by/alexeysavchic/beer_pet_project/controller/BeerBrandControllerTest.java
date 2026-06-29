package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.AddBeerBrandRequest;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.BeerBrandService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerBrandController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class BeerBrandControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private BeerBrandService beerBrandService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class AddNewBeerTests
    {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulAddBeerBrandRequest() throws Exception {
            AddBeerBrandRequest request = new AddBeerBrandRequest("validBeerName", "description");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/brand").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesBeerBrandRequest() throws Exception {
            AddBeerBrandRequest request = new AddBeerBrandRequest("validBeerName", "description");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/brand").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void invalidAddBeerBrandRequest() throws Exception {
            AddBeerBrandRequest request = new AddBeerBrandRequest("", "description");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/brand").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.brandName", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.brandName", hasItem("beer brand name must be between 1 and 30 symbols")));
        }
    }

    @Nested
    class getBeerBrandsTests
    {
        @Test
        void successfulGetBeerBrands() throws Exception {
            AddBeerBrandRequest request = new AddBeerBrandRequest("", "description");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/beer/brand").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.brandName", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.brandName", hasItem("beer brand name must be between 1 and 30 symbols")));
        }
    }

    @Nested
    class DeleteBeerBrandsTests
    {
        @Test
        @WithMockUser(username = "adminUser@gmail.com", authorities = {"ROLE_ADMIN"})
        void successfulDeleteBeerBrandRequest() throws Exception {

            mockMvc.perform(delete("/api/v1/beer/brand/brandName")).
                    andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void insufficientPrivilegesDeleteBeerBrandRequest() throws Exception {

            mockMvc.perform(delete("/api/v1/beer/brand//brandName")).
                    andExpect(status().isForbidden());
        }
    }
}
