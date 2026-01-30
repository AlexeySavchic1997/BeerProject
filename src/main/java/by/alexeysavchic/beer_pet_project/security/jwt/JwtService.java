package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.exception.ErrorMessages;
import by.alexeysavchic.beer_pet_project.exception.ExpiredJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.InvalidTokenException;
import by.alexeysavchic.beer_pet_project.exception.SecurityJwtException;
import by.alexeysavchic.beer_pet_project.exception.UnsupportedJwtTokenException;
import by.alexeysavchic.beer_pet_project.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService
{
    private static final Logger logger= LogManager.getLogger(JwtService.class);

    private final JwtConfig config;

    public ResponseCookie createBaseCookie(String email)
    {
        return ResponseCookie.from("baseToken", generateBaseToken(email))
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(900)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();
    }

    public ResponseCookie createRefreshCookie(String email)
    {
        return ResponseCookie.from("refreshToken", generateRefreshToken(email))
                .httpOnly(true)
                .secure(false)
                .path("/refresh")
                .maxAge(86400)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();
    }

    private String generateBaseToken(String email)
    {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusSeconds(config.getBaseTokenExpiresIn()).
                atZone(ZoneId.systemDefault()).toInstant());
        return  Jwts.builder().
                expiration(timeOfExpiration).
                subject(email).
                signWith(getSignInKey()).compact();
    }

    private String generateRefreshToken(String email)
    {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusSeconds(config.getRefreshTokenExpiresIn()).
                atZone(ZoneId.systemDefault()).toInstant());
        return  Jwts.builder().
                expiration(timeOfExpiration).
                subject(email).
                signWith(getSignInKey()).compact();
    }

    public String getEmailFromToken(String token)
    {
        Claims claims=Jwts.parser().
                    verifyWith(getSignInKey()).
                    build().
                    parseSignedClaims(token).
                    getPayload();
            return claims.getSubject();
    }

    public boolean validateJwtToken(String token)
    {
        try {
            Jwts.parser().
                    verifyWith(getSignInKey()).
                    build().
                    parseSignedClaims(token).
                    getPayload();
            return true;
        }
        catch (ExpiredJwtException e)
        {
            logger.error(ErrorMessages.expiredToken, new ExpiredJwtTokenException());
        }
        catch (UnsupportedJwtException e)
        {
            logger.error(ErrorMessages.unsupportedToken, new UnsupportedJwtTokenException());
        }
        catch (MalformedJwtException e)
        {
            logger.error(ErrorMessages.malformedToken, new ExpiredJwtTokenException());
        }
        catch (SecurityException e)
        {
            logger.error(ErrorMessages.securityException, new SecurityJwtException());
        }
        catch (Exception e)
        {
            logger.error(ErrorMessages.invalidToken, new InvalidTokenException());
        }
        return false;
    }

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(config.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
