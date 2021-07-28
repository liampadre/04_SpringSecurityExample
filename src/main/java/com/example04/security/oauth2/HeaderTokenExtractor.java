package com.example04.security.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Extract the token from the request header Authorization
 */
@Component
public class HeaderTokenExtractor extends AbstractTokenExtractor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderTokenExtractor.class);

    /**
     * Token must come on header
     */
    @Override
    protected String extractToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.isEmpty(authorization)) {
            String[] authHeaderSplit = authorization.split(" ");
            if (authHeaderSplit.length != 2) {
                LOGGER.info("Invalid Authentication header format in request {}. The prefix and the value must be separated by white space.", request.getRequestURL());
                return null;
            }

            try {
                TokenType type = TokenType.valueOf(authHeaderSplit[0].toUpperCase());
                LOGGER.debug("Extracting Token in header {}. " +
                        "Token of type '{}' found.", AUTHORIZATION_HEADER, type);
                // return new TokenWrapper(authHeaderSplit[1], type).getWrappedToken();
                request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, type.getDesc());
                String rawToken = (new TokenWrapper(authHeaderSplit[1], type)).getRawToken();
                int commaIndex = rawToken.indexOf(44);
                if (commaIndex > 0) {
                    rawToken = rawToken.substring(0, commaIndex);
                }
                return rawToken;
            } catch (IllegalArgumentException e) {
                LOGGER.info("Extracting Token for request {}. " +
                        "Authorization header found but it does not starts with any accepted PREFIX: {}",
                        request.getRequestURL(), TokenType.getAllowedTypes(), e);
            }
        }
        return null;
    }
}
