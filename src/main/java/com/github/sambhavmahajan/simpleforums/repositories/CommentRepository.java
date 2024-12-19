package com.github.sambhavmahajan.simpleforums.repositories;

import com.github.sambhavmahajan.simpleforums.models.Comment;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment>  findAllByParent(Comment parent);
    List<Comment> findAllChildrenById(Long id);
    void deleteById(Long id);
    Optional<Comment> findById(Long id);
    List<Comment> findAllByAuthor(ForumUser author);
}
