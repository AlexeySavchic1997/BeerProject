package by.alexeysavchic.beer_pet_project.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

    @Getter
    @Setter
    @Component
    @ConfigurationProperties(prefix = "com.group.project")
    public class CookieJwtConfig {
        private JwtConfiguration jwt;
        private CookieConfiguration cookie;

        @Getter
        @Setter
        public static class JwtConfiguration {
            private String secret;
            private int expiresIn;
        }

        @Getter
        @Setter
        public static class CookieConfiguration {
            private String name;
            private int expiresIn;
        }
    }

