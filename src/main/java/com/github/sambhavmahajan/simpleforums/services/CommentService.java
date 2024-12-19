package com.github.sambhavmahajan.simpleforums.services;

import com.github.sambhavmahajan.simpleforums.models.Comment;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.repositories.CommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    public List<Comment> getAllCommentsOfAuthor(ForumUser author) {
        return commentRepository.findAllByAuthor(author);
    }
    public Comment getCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }
    @Transactional
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }
    public void remove(Comment comment) {
        commentRepository.delete(comment);
    }

    public void update(Comment comment) {
        commentRepository.save(comment);
    }
}
