package by.alexeysavchic.beer_pet_project.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "com.group.project")
public class JwtConfig {
    private String secret;
    private long baseTokenExpiresIn;
    private long refreshTokenExpiresIn;
}

