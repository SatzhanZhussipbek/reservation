package jusan.reservation.config;

import jusan.reservation.filter.JwtAuthenticationFilter;
import jusan.reservation.security.AuthProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
        http.csrf().disable().cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests()
                .requestMatchers("/v3/api-docs/**",
                        "/swagger-ui/**", "/swagger-ui.html",
                        "/signup", "/authenticate",
                        "/h2-console/**", "/h2-console/").permitAll()// разрешить доступ всем
                .anyRequest().authenticated() // любой запрос требует авторизации
                .and()
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilterFilter, UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

}
