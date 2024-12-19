package com.github.sambhavmahajan.simpleforums.security.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {
    Token findByTokenStr(String tokenStr);
    void deleteByTokenStr(String tokenStr);
    Token findByUsername(String username);
}
