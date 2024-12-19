package com.github.sambhavmahajan.simpleforums.repositories;

import com.github.sambhavmahajan.simpleforums.models.Community;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthor(ForumUser author);
    List<Post> findAllByTitleLike(String title);
    List<Post> findAllByContentLike(String content);
    List<Post> findAllByCommunity(Community community);
    Optional<Post> findById(Long id);
}
