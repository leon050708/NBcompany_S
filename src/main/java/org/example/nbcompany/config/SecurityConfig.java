package org.example.nbcompany.config;

import org.example.nbcompany.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置 AuthenticationProvider，它会使用我们自定义的 UserDetailsService 和 PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 配置 URL 的授权规则
                .authorizeHttpRequests(authz -> authz
                        // 允许匿名访问用户注册接口
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                        // 其他所有请求都需要身份验证
                        .anyRequest().authenticated()
                )

                // 3. 启用表单登录，Spring Security 会提供一个默认的 /login 页面
                .formLogin(Customizer.withDefaults());

        // 4. 将我们自定义的 authenticationProvider 注册到 HttpSecurity
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}