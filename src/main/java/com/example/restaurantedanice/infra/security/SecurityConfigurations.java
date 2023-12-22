package com.example.restaurantedanice.infra.security;

import com.example.restaurantedanice.domain.usuario.AutenticacaoService;
import com.example.restaurantedanice.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET, "/", "/static/**", "/index.html", "/api/users/me").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/login", "/api/users/{username}", "/api/users/logout", "/api/customers", "/api/storages").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/customers", "/api/storages").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/customers/{id}", "/api/storages/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}", "/api/storages/{id}", "/api/customers/{id}").authenticated()
                        .anyRequest().denyAll());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

