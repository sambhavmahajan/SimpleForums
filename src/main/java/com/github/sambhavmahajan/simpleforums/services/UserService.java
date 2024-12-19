package com.github.sambhavmahajan.simpleforums.services;

import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepo;
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    public ForumUser getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    @Transactional
    public void deleteUserByUsername(String username) {
        userRepo.deleteByUsername(username);
    }
    @Transactional
    public void saveUser(ForumUser user) {
        userRepo.save(user);
    }
}
