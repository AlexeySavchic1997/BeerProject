package by.alexeysavchic.beer_pet_project.mapper;

import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.security.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserMapperTest {

    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(mapper, "passwordEncoder", passwordEncoder);
    }

    @Test
    void shouldMapUserRegisterRequestToUserWithEncodedPasswordAndDefaultRole() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setPassword("mySecretPassword");
        when(passwordEncoder.encode("mySecretPassword")).thenReturn("encoded_hash_123");

        User user = mapper.userRegisterRequestToUser(request);

        assertNotNull(user);
        assertEquals("encoded_hash_123", user.getPassword());
        verify(passwordEncoder, times(1)).encode("mySecretPassword");
        assertNotNull(user.getRoles());
        assertEquals(1, user.getRoles().size());
        assertTrue(user.getRoles().contains(Role.ROLE_USER));
    }
}