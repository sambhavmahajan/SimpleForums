package com.github.sambhavmahajan.simpleforums.security.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenContainer {
    //token, TokenObj
    private final ConcurrentHashMap<String, Token> tokenMap;
    private final TokenRepo tokenRepo;
    public TokenContainer(ConcurrentHashMap<String, Token> tokenMap, TokenRepo tokenRepo) {
        this.tokenMap = tokenMap;
        this.tokenRepo = tokenRepo;
    }
    public boolean isValidToken(String token) {
        if(token == null){
            return false;
        }
        Token tokenObj = tokenMap.get(token);
        if(tokenObj == null) {
            return false;
        }
        if(!tokenObj.isValid()) {
            tokenRepo.deleteByTokenStr(token);
            tokenMap.remove(token);
            return false;
        }
        return true;
    }
    @Transactional
    public void add(Token token) {
        Token oldToken = tokenRepo.findByUsername(token.getUsername());
        if(oldToken != null) {
            tokenRepo.delete(oldToken);
        }
        tokenMap.put(token.getTokenStr(), token);
        tokenRepo.save(token);
    }
    public Token userTokenExists(String username) {
        return tokenRepo.findByUsername(username);
    }
    @Transactional
    public void remove(Token token) {
        tokenRepo.delete(token);
        if(tokenMap.get(token.getTokenStr()) != null) {
            tokenMap.remove(token.getTokenStr());
        }
    }
    public Token getToken(String tokenStr) {
        if(tokenStr == null) {
            return null;
        }
        return tokenMap.get(tokenStr);
    }
}
