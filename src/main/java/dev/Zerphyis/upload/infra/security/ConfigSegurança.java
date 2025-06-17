package dev.Zerphyis.upload.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ConfigSegurança {
    @Bean
    public SecurityFilterChain filtrosSeguranca(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(req -> {
                    req.requestMatchers("/Estilos/**",
                            "/", "/index", "/home", "/esqueci-minha-senha").permitAll();
                    req.anyRequest().authenticated();
                })
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe.key("lembrarDeMim")
                        .alwaysRemember(true)
                )
                .csrf(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder codificadorSenha(){
        return new BCryptPasswordEncoder();
    }
}
