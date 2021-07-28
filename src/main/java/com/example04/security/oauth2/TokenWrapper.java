package com.example04.security.oauth2;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TokenWrapper {

    private static final String TOKEN_TYPE_PREFIX = "type";

    private final String rawToken;
    private final TokenType tokenType;

    public TokenWrapper(String rawToken, TokenType tokenType) {
        this.rawToken = rawToken;
        this.tokenType = tokenType;
    }

    public static TokenWrapper buildFromWrappedToken(String tokenValue){
        for(TokenType tokenType : TokenType.getAllowedTypes()){
            List<String> prefixes = getAllowedPrefixesFor(tokenType);
            for(String prefix: prefixes) {
                if (tokenValue.startsWith(prefix)){
                    return new TokenWrapper(tokenValue.substring(prefix.length()), tokenType);
                }
            }
        }

        return null;
    }

    /**
     * It builds a String with the token type and the rawtoken with the following format
     * type=tsec;XXXXXXXXX
     * type=jwt;XXXXXXXXXX
     *
     * or this is underscore is true
     * type_tsec_XXXXXXXXX
     * type_jwt_XXXXXXXXXX
     *
     *
     * @return
     */
    public String getWrappedToken(boolean underscore){
        StringBuilder prefix = new StringBuilder(getPrefixFor(this.tokenType, underscore)).append(rawToken);
        return prefix.toString();
    }

    public String getWrappedToken(){
        return getWrappedToken(false);
    }

    /**
     * It builds the complete prefix for the token. In case of parameter extractor, we use the underscore
     *
     *  @return
     */
    private static String getPrefixFor(TokenType tokenType, boolean underscoreChar){
        StringBuilder prefix = new StringBuilder(TOKEN_TYPE_PREFIX);
        if(underscoreChar){
            prefix.append('_');
        } else{
            prefix.append('=');
        }
        prefix.append(tokenType.getDesc());
        if(underscoreChar){
            prefix.append('_');
        } else{
            prefix.append(';');
        }
        return prefix.toString();
    }

    private static List<String> getAllowedPrefixesFor(TokenType tokenType){
        return Arrays.asList(getPrefixFor(tokenType, true), getPrefixFor(tokenType, false));
    }

    public String getRawToken() {
        return rawToken;
    }

    public TokenType getTokenType() {
        return tokenType;
    }
}
