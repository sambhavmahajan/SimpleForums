package com.github.sambhavmahajan.simpleforums.security.session;

import com.github.sambhavmahajan.simpleforums.ForumConstants;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.repositories.UserRepository;
import com.github.sambhavmahajan.simpleforums.security.token.Token;
import com.github.sambhavmahajan.simpleforums.security.token.TokenContainer;
import com.github.sambhavmahajan.simpleforums.security.token.TokenRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionManager {
    private final UserRepository userRepository;
    private final TokenContainer tokenContainer;
    public SessionManager(TokenContainer tokenContainer, UserRepository userRepository) {
        this.tokenContainer = tokenContainer;
        this.userRepository = userRepository;
    }
    public String currentSessionTokenString(HttpSession session) {
        return (String) session.getAttribute(ForumConstants.TokenVariableName);
    }
    public boolean isValidSession(HttpSession session) {
        String tokenString = currentSessionTokenString(session);
        return tokenContainer.isValidToken(tokenString);
    }
    @Transactional
    public boolean createSession(HttpSession session, String username, String password) {
        ForumUser usr = userRepository.findByUsername(username);
        if(usr != null && usr.getPassword().equals(password)) {
            Token tkn = tokenContainer.userTokenExists(username);
            if(tkn != null) {
                tokenContainer.remove(tkn);
            }
            tkn = new Token(username);
            tokenContainer.add(tkn);
            session.setAttribute(ForumConstants.TokenVariableName, tkn.getTokenStr());
            return true;
        }
        return false;
    }
    public String getCurrentUser(HttpSession session) {
        String tokenString = currentSessionTokenString(session);
        Token token = tokenContainer.getToken(tokenString);
        if(token == null) return null;
        return tokenContainer.getToken(tokenString).getUsername();
    }
}
