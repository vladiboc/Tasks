package org.example.tasks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  public ReactiveAuthenticationManager reactiveAuthenticationManager(
      final ReactiveUserDetailsService userDetailsService, final PasswordEncoder passwordEncoder
  ) {
    final var reactiveAuthenticationManager =
        new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    reactiveAuthenticationManager.setPasswordEncoder(passwordEncoder);
    return reactiveAuthenticationManager;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
      ServerHttpSecurity http, ReactiveAuthenticationManager authenticationManager
  ) {
    return this.buildHttpSecurity(http)
        .authenticationManager(authenticationManager)
        .build();
  }

  private ServerHttpSecurity buildHttpSecurity(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange((auth) -> auth
            .pathMatchers(HttpMethod.GET, "/swagger/**").permitAll()
            .pathMatchers(HttpMethod.GET, "/webjars/**").permitAll()
            .pathMatchers(HttpMethod.GET, "/v3/api-docs/**").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/v1/users/**").permitAll()
            .pathMatchers("/api/v1/**").hasAnyRole("USER", "MANAGER")
            .anyExchange().authenticated())
        .httpBasic(Customizer.withDefaults());
  }
}
