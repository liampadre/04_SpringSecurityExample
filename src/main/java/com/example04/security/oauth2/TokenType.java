package com.example04.security.oauth2;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    JWT ("jwt"),
    BEARER ("bearer"),
    LIGHTWEIGHT ("lightweight");

    private final String desc;

    TokenType(String desc){
        this.desc = desc;
    }

    /**
     * Find a tokenType in case INSENSITIVE way
     * @param tokenType name
     * @return the TokenType
     */
    public static TokenType lookup(String tokenType) {
        return TokenType.valueOf(tokenType.toUpperCase());
    }

    public String getDesc(){
        return this.desc;
    }

    public static List<TokenType> getAllowedTypes(){
        return Arrays.asList(JWT, BEARER, LIGHTWEIGHT);
    }
}
