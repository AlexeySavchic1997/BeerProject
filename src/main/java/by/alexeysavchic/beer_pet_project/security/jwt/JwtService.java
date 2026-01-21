package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.dto.JwtAuthentificationDTO;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService
{
    private static final Logger logger= LogManager.getLogger(JwtService.class);
    @Value("b1148302f5d1c2a3234b97b8e9f07478")
    String jwtSecret;

    public JwtAuthentificationDTO generateAuthToken(String email)
    {
        JwtAuthentificationDTO jwtDTO=new JwtAuthentificationDTO();
        jwtDTO.setToken(generateJwtToken(email));
        jwtDTO.setRefreshToken(generateRefreshJwtToken(email));
        return jwtDTO;
    }

    public JwtAuthentificationDTO refreshBaseToken(String email, String refreshToken)
    {
        JwtAuthentificationDTO jwtDTO=new JwtAuthentificationDTO();
        jwtDTO.setToken(generateJwtToken(email));
        jwtDTO.setRefreshToken(refreshToken);
        return jwtDTO;
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

    private String generateJwtToken(String email)
    {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusMinutes(30).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().
                setSubject(email).
                setExpiration(timeOfExpiration).
                signWith(getSignInKey()).compact();
    }

    private String generateRefreshJwtToken(String email)
    {
        Date timeOfExpiration = Date.from(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder().
                setSubject(email).
                setExpiration(timeOfExpiration).
                signWith(getSignInKey()).compact();
    }

    private SecretKey getSignInKey()
    {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
