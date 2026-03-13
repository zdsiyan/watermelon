package com.github.watermelon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 配置类
 * 替代原有的Spring Security 4.x配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密码编码器
     * 使用BCrypt算法
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全过滤器链配置
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 禁用CSRF（简化配置，生产环境应根据需要启用）
            .csrf(AbstractHttpConfigurer::disable)

            // 授权配置
            .authorizeHttpRequests(authz -> authz
                // 静态资源允许匿名访问
                .requestMatchers(
                    "/skin/**",
                    "/static/**",
                    "/favicon.ico",
                    "/error"
                ).permitAll()

                // 公开页面允许匿名访问
                .requestMatchers(
                    "/",
                    "/index",
                    "/signin",
                    "/register",
                    "/user/login",
                    "/user/register"
                ).permitAll()

                // 其他请求需要认证
                .anyRequest().authenticated()
            )

            // 表单登录配置
            .formLogin(form -> form
                .loginPage("/signin")
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/home")
                .failureUrl("/signin?error=true")
                .permitAll()
            )

            // 注销配置
            .logout(logout -> logout
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/signin")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // 会话管理
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        return http.build();
    }
}