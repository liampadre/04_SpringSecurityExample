package com.example04.security.oauth2;

import lombok.RequiredArgsConstructor;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {

    private final AuthenticationManager tokenAuthenticationManager;

    private final HeaderTokenExtractor headerTokenExtractor;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("oauth2-resource");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
//            .addFilterBefore(new TokenAuthenticationProcessingFilter(
//                    authenticationManager,
//                    Arrays.asList(new HeaderTokenExtractor())
//            ), AbstractPreAuthenticatedProcessingFilter.class) //TODO Testing of this, should work with jwt, bearer o lightweight token type
            .addFilterAt(new TokenAuthenticationProcessingFilter(
                    tokenAuthenticationManager,
                    Arrays.asList(headerTokenExtractor)
            ), AbstractPreAuthenticatedProcessingFilter.class) //TODO Testing of this, should work with jwt, bearer o lightweight token type
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/products", "/users", "/users/**").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/products", "/users").hasAnyRole("ADMIN", "ADMIN_CLIENT")
                .antMatchers(HttpMethod.GET, "/employees").access("#oauth2.hasScope('write')")
                .antMatchers("/h2-console/**", "/clients", "/favicon.ico").permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().frameOptions().sameOrigin();
    }
}
