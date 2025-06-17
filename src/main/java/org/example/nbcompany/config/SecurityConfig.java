package org.example.nbcompany.config;

import org.example.nbcompany.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // 启用方法级别的安全注解，如 @PreAuthorize
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

    /**
     * expose AuthenticationManager bean
     * used for manual authentication, e.g., in login endpoint
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用 CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // 2. 配置 URL 的授权规则
                .authorizeHttpRequests(authz -> authz
                        // 允许匿名访问认证相关接口
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/register/**").permitAll() // 企业注册、用户注册
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll() // 用户登录
                        .requestMatchers(HttpMethod.GET, "/api/v1/companies").permitAll() // 获取企业列表
                        .requestMatchers("/error").permitAll() // Spring Boot 错误页面

                        // 管理员接口
                        // 平台超级管理员接口
                        .requestMatchers("/api/v1/admin/**").hasRole("SUPER_ADMIN")
                        // 企业管理员接口
                        .requestMatchers("/api/v1/company/{companyId}/members/**").hasRole("COMPANY_ADMIN")

                        // 其他所有请求都需要身份验证
                        .anyRequest().authenticated()
                )

                // 3. 启用表单登录，Spring Security 会提供一个默认的 /login 页面
                // 我们不再依赖默认的 /login 页面，而是使用自定义的 /api/v1/auth/login 接口
                // .formLogin(Customizer.withDefaults()); // 不再需要默认表单登录，因为我们将手动处理登录
                // 禁用默认的HTTP Basic认证
                .httpBasic(AbstractHttpConfigurer::disable)
                // 禁用默认的Session管理（如果使用JWT）
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


                // 4. 将我们自定义的 authenticationProvider 注册到 HttpSecurity
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}