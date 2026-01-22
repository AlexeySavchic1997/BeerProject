package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.dto.response.JwtAuthentificationDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService
{
    private static final Logger logger= LogManager.getLogger(JwtService.class);
    @Value("8b8bc66f6b3525d94e39f4927e518f75c7769409388c4e62868fd6bc3e781220f141edc1")
    String jwtSecret;

    public ResponseCookie generateJwtCookie(String email)
    {
        String jwt= Jwts.builder().
                setSubject(email).
                signWith(getSignInKey()).compact();

        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();
        return cookie;
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
            logger.error("Expired JWT Exception", e);
        }
        catch (UnsupportedJwtException e)
        {
            logger.error("Unsupported JWT Exception");
        }
        catch (MalformedJwtException e)
        {
            logger.error("Malformed JWT Exception", e);
        }
        catch (SecurityException e)
        {
            logger.error("Security exception", e);
        }
        catch (Exception e)
        {
            logger.error("Invalid token", e);
        }
        return false;
    }

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
