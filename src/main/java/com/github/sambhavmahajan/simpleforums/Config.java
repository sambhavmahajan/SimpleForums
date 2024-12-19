package com.github.sambhavmahajan.simpleforums;

import com.github.sambhavmahajan.simpleforums.security.token.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Config {
    @Bean
    public ConcurrentHashMap<String, Token> tokenMap() {
        return new ConcurrentHashMap<String, Token>();
    }
}
