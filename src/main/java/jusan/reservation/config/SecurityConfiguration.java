package jusan.reservation.config;

import jusan.reservation.filter.JwtAuthenticationFilter;
import jusan.reservation.security.AuthProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthProviderImpl authProvider;

    private final JwtAuthenticationFilter jwtAuthenticationFilterFilter;

    @Autowired
    public SecurityConfiguration(AuthProviderImpl authProvider, JwtAuthenticationFilter jwtAuthenticationFilterFilter) {
        this.authProvider = authProvider;
        this.jwtAuthenticationFilterFilter = jwtAuthenticationFilterFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/v3/api-docs/**",
                        "/swagger-ui/**", "/swagger-ui.html",
                        "/signup", "/signin", "/image/upload**", "/image/url**", "/image/path**",
                        "/h2-console/**", "/h2-console/").permitAll()// разрешить доступ всем
                .anyRequest().authenticated() // любой запрос требует авторизации
                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilterFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().sameOrigin();
        http.headers().httpStrictTransportSecurity().disable();
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                // ...
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/resources/**", "/signup", "/signin", "/image/upload/**", "/image/url/**", "/image/path/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        //.requestMatchers("/db/**").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') and hasRole('DBA')"))
                        // .requestMatchers("/db/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("DBA")))
                        .anyRequest().denyAll()
                );

        return http.build();
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

}
