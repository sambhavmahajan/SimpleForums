package com.github.sambhavmahajan.simpleforums.security.token;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Entity
public class Token {
    @Id
    @GeneratedValue
    private long id;
    private String tokenStr;
    private String username;
    private LocalDateTime creation;
    public Token() {}
    public Token(String username){
        this.username = username;
        this.creation = LocalDateTime.now();
        this.tokenStr = username + creation.toString();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tokenStr.getBytes());
            tokenStr = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Could not get SHA-256 algorithm.");
        }
    }
    public boolean isValid() {
        long validity = creation.until(LocalDateTime.now(), ChronoUnit.HOURS);
        return validity < 5;
    }
    public String getUsername() {
        return this.username;
    }
    public String getTokenStr() {
        return this.tokenStr;
    }

}
