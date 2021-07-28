package com.example04.security;

import lombok.RequiredArgsConstructor;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example04.security.jwt.JwtAuthorizationFilter;
import com.example04.service.CustomClientDetailsService;
import com.example04.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@Order(1)
@RequiredArgsConstructor
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final CustomUserDetailsService customUserDetailsService;

    private final CustomClientDetailsService customClientDetailsService;

    private final PasswordEncoder passwordEncoder;

//    private final TokenStore tokenStore;

    private final DataSource dataSource;

//    private final AccessDeniedHandler accessDeniedHandler;

//    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean("defaultAuthenticationManager")
    @Primary
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("tokenAuthenticationManager")
    public AuthenticationManager tokenAuthenticationManager() {
        OAuth2AuthenticationManager manager = new OAuth2AuthenticationManager();
        manager.setResourceId("oauth2-resource");
        manager.setTokenServices(tokenServices());
        manager.setClientDetailsService(customClientDetailsService);
        return manager;
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(customClientDetailsService);
        services.setSupportRefreshToken(true);
        services.setTokenStore(tokenStore());
        return services;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .requestMatchers()
                .antMatchers("/login", "/oauth/authorize")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic()
//                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and()
//                .authorizeRequests()
//                    .antMatchers(HttpMethod.GET, "/products", "/users", "/users/**").hasAnyRole("USER", "ADMIN")
//                    .antMatchers(HttpMethod.POST, "/products", "/users").hasRole("ADMIN")
//                    .antMatchers("/h2-console/**").permitAll() // Es necesario para habilitar la /h2-console
//                    .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                    .headers().frameOptions().sameOrigin()// Es necesario para permitir que la /h2-console cargue en varios frames
//                .and()
//                    .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
//                .and()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                    .csrf().disable();
//        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
//    }
}
