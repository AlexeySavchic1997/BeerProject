package by.alexeysavchic.beer_pet_project.security.jwt;

import by.alexeysavchic.beer_pet_project.security.CookieJwtConfig;
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
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService
{
    private static final Logger logger= LogManager.getLogger(JwtService.class);

    private final CookieJwtConfig config;

    public JwtService(CookieJwtConfig config) {
        this.config = config;
    }

    public String generateJwtToken(String email)
    {
        return  Jwts.builder().
                setSubject(email).
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
        byte[] keyBytes = Decoders.BASE64.decode(config.getJwt().getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
