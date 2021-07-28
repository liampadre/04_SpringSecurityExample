package com.example04.security.oauth2;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public abstract class AbstractTokenExtractor implements TokenExtractor {

	@Override
	public Authentication extract(HttpServletRequest request) {
	    String tokenValue = extractToken(request);
	    if (tokenValue != null) {
	        return new PreAuthenticatedAuthenticationToken(tokenValue, "");
	    }
	    return null;
	}

	protected abstract String extractToken(HttpServletRequest request);

}
