package com.github.sambhavmahajan.simpleforums.services;

import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.models.Post;
import com.github.sambhavmahajan.simpleforums.repositories.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    @Transactional
    public void createPost(Post post) {
        postRepository.save(post);
    }
    public List<Post> getAllPostByAuthor(ForumUser author) {
        return postRepository.findAllByAuthor(author);
    }
    public Post getPostById(long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void updatePost(Post post) {
        postRepository.save(post);
    }
}
