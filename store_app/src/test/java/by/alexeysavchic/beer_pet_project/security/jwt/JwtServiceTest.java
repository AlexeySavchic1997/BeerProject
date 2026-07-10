package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.exception.ExpiredJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.InvalidTokenException;
import by.alexeysavchic.beer_pet_project.exception.MalformedJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.SecurityJwtException;
import by.alexeysavchic.beer_pet_project.exception.UnsupportedJwtTokenException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private JwtConfig config;

    @InjectMocks
    private JwtService jwtService;

    private final String VALID_SECRET = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIHNlY3JldCBrZXkgZm9yIEpXVCB0ZXN0aW5nIQ==";
    private final String TEST_EMAIL = "test@gmail.com";

    @BeforeEach
    void setUp() {
        lenient().when(config.getSecret()).thenReturn(VALID_SECRET);
    }

    @Nested
    class generateBaseTokenTests {
        @Test
        void successfulGenerateBaseTokenTest() {
            when(config.getBaseTokenExpiresIn()).thenReturn(900L);

            String token = jwtService.generateBaseToken(TEST_EMAIL);

            assertNotNull(token);
            assertEquals(3, token.split("\\.").length);
        }
    }

    @Nested
    class generateRefreshTokenTests {
        @Test
        void successfulGenerateRefreshTokenTest() {
            when(config.getRefreshTokenExpiresIn()).thenReturn(86400L);

            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            assertNotNull(token);
            assertEquals(3, token.split("\\.").length);
        }
    }

    @Nested
    class getEmailFromTokenTests {
        @Test
        void successfullyExtractsEmail() {
            when(config.getBaseTokenExpiresIn()).thenReturn(900L);
            String token = jwtService.generateBaseToken(TEST_EMAIL);

            String extractedEmail = jwtService.getEmailFromToken(token);

            assertEquals(TEST_EMAIL, extractedEmail);
        }
    }

    @Nested
    class getTypeFromTokenTests {
        @Test
        void successfullyExtractsBaseType() {
            when(config.getBaseTokenExpiresIn()).thenReturn(900L);
            String token = jwtService.generateBaseToken(TEST_EMAIL);

            String type = jwtService.getTypeFromToken(token);

            assertEquals("Base", type);
        }

        @Test
        void successfullyExtractsRefreshType() {
            when(config.getRefreshTokenExpiresIn()).thenReturn(86400L);
            String token = jwtService.generateRefreshToken(TEST_EMAIL);

            String type = jwtService.getTypeFromToken(token);

            assertEquals("Refresh", type);
        }
    }

    @Nested
    class validateJwtTokenTests {

        @Test
        void successfulValidation() {
            when(config.getBaseTokenExpiresIn()).thenReturn(900L);
            String validToken = jwtService.generateBaseToken(TEST_EMAIL);

            boolean isValid = jwtService.validateJwtToken(validToken);

            assertTrue(isValid);
        }

        @Test
        void throwsMalformedJwtTokenExceptionForGarbageString() {
            String garbageToken = "this.is.not.a.valid.jwt.token";

            assertThrows(MalformedJwtTokenException.class, () -> jwtService.validateJwtToken(garbageToken));
        }

        @Test
        void throwsExpiredJwtTokenExceptionForOldToken() {
            String expiredToken = createCustomToken(Instant.now().minus(1, ChronoUnit.HOURS), getSignInKey());

            assertThrows(ExpiredJwtTokenException.class, () -> jwtService.validateJwtToken(expiredToken));
        }

        @Test
        void throwsSecurityJwtExceptionForWrongSignature() {
            String HACKER_SECRET = "SGFja2VyS2V5VGhhdElzQWxzbzI1NkJpdHNMZW5ndGhGb3JUZXN0aW5n";
            SecretKey hackerKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(HACKER_SECRET));

            String forgedToken = createCustomToken(Instant.now().plus(1, ChronoUnit.HOURS), hackerKey);

            assertThrows(SecurityJwtException.class, () -> jwtService.validateJwtToken(forgedToken));
        }

        @Test
        void throwsUnsupportedJwtTokenExceptionForUnsignedToken() {
            String unsignedToken = Jwts.builder()
                    .subject(TEST_EMAIL)
                    .compact();

            assertThrows(UnsupportedJwtTokenException.class, () -> jwtService.validateJwtToken(unsignedToken));
        }

        @Test
        void throwsInvalidTokenExceptionForNullOrEmptyToken() {
            assertThrows(InvalidTokenException.class, () -> jwtService.validateJwtToken(null));
            assertThrows(InvalidTokenException.class, () -> jwtService.validateJwtToken(""));
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(VALID_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createCustomToken(Instant expirationTime, SecretKey signingKey) {
        return Jwts.builder()
                .subject(TEST_EMAIL)
                .expiration(Date.from(expirationTime))
                .signWith(signingKey)
                .compact();
    }
}
