package by.alexeysavchic.beer_pet_project.controller;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtResponseDTO;
import by.alexeysavchic.beer_pet_project.entity.enums.Gender;
import by.alexeysavchic.beer_pet_project.entity.enums.Location;
import by.alexeysavchic.beer_pet_project.exception.EmailAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.RefreshTokenIsAbsentException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.security.CustomUserDetailsService;
import by.alexeysavchic.beer_pet_project.security.SecurityConfig;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtFilter;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtFilter.class})
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Nested
    class SignUpTests {
        @Test
        void successfulRegisterRequest() throws Exception {
            UserRegisterRequest request = new UserRegisterRequest("validUsername", "validEmail@gmail.com", "validPass1234", Location.BLR, Gender.MALE);
            String jsonBody = objectMapper.writeValueAsString(request);
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO("mockBaseToken", "mockRefreshToken");

            when(authService.signUp(any(UserRegisterRequest.class))).thenReturn(jwtResponseDTO);

            mockMvc.perform(post("/api/v1/auth/signup").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$.baseToken").value("mockBaseToken")).
                    andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
        }

        @Test
        void invalidRegisterRequest() throws Exception {
            UserRegisterRequest request = new UserRegisterRequest("", "", "", null, null);
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/auth/signup").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$.username", hasItem("username must be between 2 and 20 symbols"))).
                    andExpect(jsonPath("$.username", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.email", hasItem("Wrong email pattern"))).
                    andExpect(jsonPath("$.email", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.password", hasItem("password must be between 8 and 15 symbols"))).
                    andExpect(jsonPath("$.password", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.userLocation", hasItem("must not be null"))).
                    andExpect(jsonPath("$.userGender", hasItem("must not be null")));

        }

        @Test
        void alreadyExistingUser() throws Exception {
            UserRegisterRequest request = new UserRegisterRequest("validUsername", "validEmail@gmail.com", "validPass1234", Location.BLR, Gender.MALE);
            String jsonBody = objectMapper.writeValueAsString(request);

            when(authService.signUp(any(UserRegisterRequest.class))).thenThrow(EmailAlreadyExistsException.class);

            mockMvc.perform(post("/api/v1/auth/signup").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }
    }

    @Nested
    class LogInTests {
        @Test
        void successfulLoginRequest() throws Exception {
            LogInRequest request = new LogInRequest("validEmail@gmail.com", "validPass1234");
            String jsonBody = objectMapper.writeValueAsString(request);
            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO("mockBaseToken", "mockRefreshToken");

            when(authService.logIn(any(LogInRequest.class))).thenReturn(jwtResponseDTO);

            mockMvc.perform(post("/api/v1/auth/login").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isOk()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$.baseToken").value("mockBaseToken")).
                    andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
        }

        @Test
        void invalidLoginRequest() throws Exception {
            LogInRequest request = new LogInRequest("", "");
            String jsonBody = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/auth/login").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isBadRequest()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$.email", hasItem("Wrong email pattern"))).
                    andExpect(jsonPath("$.email", hasItem("must not be blank"))).
                    andExpect(jsonPath("$.password", hasItem("password must be between 8 and 15 symbols"))).
                    andExpect(jsonPath("$.password", hasItem("must not be blank")));
        }


        @Test
        void wrongPasswordRequest() throws Exception {
            LogInRequest request = new LogInRequest("validEmail@gmail.com", "invalidPass");
            String jsonBody = objectMapper.writeValueAsString(request);

            when(authService.logIn(any(LogInRequest.class))).thenThrow(WrongPasswordException.class);

            mockMvc.perform(post("/api/v1/auth/login").
                            contentType(MediaType.APPLICATION_JSON_VALUE).
                            content(jsonBody)).
                    andExpect(status().isForbidden());
        }
    }

    @Nested
    class RefreshTests {
        @Test
        void successfulRefreshRequest() throws Exception {

            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO("mockBaseToken", "mockRefreshToken");
            when(authService.refresh(any(HttpServletRequest.class))).thenReturn(jwtResponseDTO);
            when(jwtService.getTypeFromToken(any())).thenReturn("Refresh");

            mockMvc.perform(get("/api/v1/auth/refresh").
                            header(HttpHeaders.AUTHORIZATION, "Bearer " + "mockRefreshToken")).
                    andExpect(status().isOk()).
                    andExpect(content().contentType(MediaType.APPLICATION_JSON)).
                    andExpect(jsonPath("$.baseToken").value("mockBaseToken")).
                    andExpect(jsonPath("$.refreshToken").value("mockRefreshToken"));
        }

        @Test
        void wrongTokenTypeRefreshRequest() throws Exception {

            JwtResponseDTO jwtResponseDTO = new JwtResponseDTO("mockBaseToken", "mockRefreshToken");
            when(authService.refresh(any(HttpServletRequest.class))).thenReturn(jwtResponseDTO);
            when(jwtService.getTypeFromToken(any(String.class))).thenReturn("Base");

            mockMvc.perform(get("/api/v1/auth/refresh").
                            header(HttpHeaders.AUTHORIZATION, "Bearer " + "mockBaseToken")).
                    andExpect(status().isForbidden()).
                    andExpect(content().string("Wrong token type. Type Base received but Refresh type expected"));
        }

        @Test
        void refreshTokenIsAbsent() throws Exception {

            when(authService.refresh(any(HttpServletRequest.class))).thenThrow(new RefreshTokenIsAbsentException());

            mockMvc.perform(get("/api/v1/auth/refresh")).
                    andExpect(status().isForbidden()).
                    andExpect(content().string("Refresh token is absent"));
        }
    }


}
