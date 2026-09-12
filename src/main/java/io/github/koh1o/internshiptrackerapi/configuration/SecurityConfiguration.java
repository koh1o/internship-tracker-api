    package io.github.koh1o.internshiptrackerapi.configuration;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.ProviderManager;
    import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.config.http.SessionCreationPolicy;

    @Configuration
    public class SecurityConfiguration {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                throws Exception {

            http.csrf(csrf -> csrf.disable());
            http.sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            http.authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(
                            "/api/auth/register",
                            "/api/auth/login",
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll()
                    .anyRequest().permitAll()
            );

            return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(
                UserDetailsService userDetailsService,
                PasswordEncoder passwordEncoder
        ) {

            DaoAuthenticationProvider daoAuthenticationProvider
                    = new DaoAuthenticationProvider(userDetailsService);

            daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

            return new ProviderManager(daoAuthenticationProvider);
        }
    }