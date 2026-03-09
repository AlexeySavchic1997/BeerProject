package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.exception.ExpiredJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.InvalidTokenException;
import by.alexeysavchic.beer_pet_project.exception.MalformedJwtTokenException;
import by.alexeysavchic.beer_pet_project.exception.SecurityJwtException;
import by.alexeysavchic.beer_pet_project.exception.UnsupportedJwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {

    private final JwtConfig config;

    public String generateBaseToken(String email) {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusSeconds(config.getBaseTokenExpiresIn()).
                atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().
                expiration(timeOfExpiration).
                claim("type", "Base").
                subject(email).
                signWith(getSignInKey()).compact();
    }

    public String generateRefreshToken(String email) {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusSeconds(config.getRefreshTokenExpiresIn()).
                atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().
                expiration(timeOfExpiration).
                claim("type", "Refresh").
                subject(email).
                signWith(getSignInKey()).compact();
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().
                verifyWith(getSignInKey()).
                build().
                parseSignedClaims(token).
                getPayload();
        return claims.getSubject();
    }

    public String getTypeFromToken(String token) {
        Claims claims = Jwts.parser().
                verifyWith(getSignInKey()).
                build().
                parseSignedClaims(token).
                getPayload();
        return claims.get("type", String.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().
                    verifyWith(getSignInKey()).
                    build().
                    parseSignedClaims(token).
                    getPayload();
            return true;
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtTokenException();
        } catch (UnsupportedJwtException e) {
            throw new UnsupportedJwtTokenException();
        } catch (MalformedJwtException e) {
            throw new MalformedJwtTokenException();
        } catch (SecurityException e) {
            throw new SecurityJwtException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(config.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
