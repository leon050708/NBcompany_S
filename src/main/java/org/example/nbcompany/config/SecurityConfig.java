package org.example.nbcompany.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF（对于无状态的 REST API 是标准操作）
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 配置 URL 的授权规则
                .authorizeHttpRequests(authz -> authz
                        // 允许对 /api/users/** 下的所有路径进行未经身份验证的访问
                        .requestMatchers("/api/users/**").permitAll()
                        // 你可以更精细地控制，比如只允许注册（POST）
                        // .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // 除了上面放行的路径，所有其他请求都需要身份验证
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}