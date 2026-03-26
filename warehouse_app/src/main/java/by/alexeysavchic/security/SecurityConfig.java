package by.alexeysavchic.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private RequestFilter requestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).
                httpBasic(httpBasic -> httpBasic.disable()).
                authorizeHttpRequests(auth -> auth.
                        anyRequest().permitAll()).
                sessionManagement(sess -> sess.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                addFilterBefore(requestFilter, RequestFilter.class);
        return http.build();
    }
}
