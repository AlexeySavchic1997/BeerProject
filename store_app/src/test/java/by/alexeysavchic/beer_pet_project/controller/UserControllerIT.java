package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
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

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class changeCredentialsTests {
        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void successfulChangeCredentials() throws Exception {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest("validUsername", "validEmail@gmail.com", "validPassword", "nValidPassword");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/api/v1/user/update").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk());
        }

        @Test
        @WithMockUser(username = "regularUser@gmail.com", authorities = {"ROLE_USER"})
        void invalidChangeCredentials() throws Exception {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest("", "", "", "");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/api/v1/user/update").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(jsonPath("$.username", hasItem("username must be between 2 and 20 symbols"))).
                    andExpect(jsonPath("$.username", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.email", hasItem("Wrong email pattern"))).
                    andExpect(jsonPath("$.email", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.oldPassword", hasItem("password must be between 8 and 15 symbols"))).
                    andExpect(jsonPath("$.oldPassword", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.newPassword", hasItem("password must be between 8 and 15 symbols"))).
                    andExpect(jsonPath("$.newPassword", hasItem("must not be blank")));
        }
    }
}
