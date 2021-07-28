package com.example04.security.oauth2;

import lombok.RequiredArgsConstructor;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.ClientAuthenticationException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class TokenAuthenticationProcessingFilter implements Filter, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProcessingFilter.class);

    private static final String MESSAGE_AUTH_REQ_FAILED = "Authentication request failed: {}";

    private final AuthenticationManager tokenAuthenticationManager;

    private final List<TokenExtractor> tokenExtractorList;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

    @Override
    public void afterPropertiesSet() {
        Assert.state(tokenAuthenticationManager != null, "Authentication manager is required");
        Assert.state(!CollectionUtils.isEmpty(tokenExtractorList), "Token extractor is required");
    }

    @Override
    public void init(FilterConfig filterConfig) {
        //Nothing to do here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        Authentication authentication;

        if ((authentication = extractAuthentication(req)) == null) {
            logger.debug("No token in request, will continue chain.");
            chain.doFilter(req, response);
            return;
        }

        try {
            req.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
            if (authentication instanceof AbstractAuthenticationToken) {
                AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken)authentication;
                needsDetails.setDetails(this.authenticationDetailsSource.buildDetails(req));
            }
            Authentication authResult = this.tokenAuthenticationManager.authenticate(authentication);
            logger.debug("Authentication success: {}", authResult);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException | ClientAuthenticationException failed) {
            SecurityContextHolder.clearContext();
            logger.info(MESSAGE_AUTH_REQ_FAILED, failed);
        }

        chain.doFilter(req, response);
    }

    private Authentication extractAuthentication(final HttpServletRequest req) {
        Authentication authentication;
        for (TokenExtractor tokenExtractor : tokenExtractorList) {
            authentication = tokenExtractor.extract(req);
            if (authentication != null) {
                return authentication;
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        //Nothing to do here
    }
}