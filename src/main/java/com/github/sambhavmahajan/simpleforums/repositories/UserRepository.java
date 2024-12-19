package com.github.sambhavmahajan.simpleforums.repositories;

import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ForumUser, Long> {
    ForumUser findByUsername(String username);
    void deleteByUsername(String username);
}
