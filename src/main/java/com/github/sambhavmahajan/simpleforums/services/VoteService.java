package com.github.sambhavmahajan.simpleforums.services;

import com.github.sambhavmahajan.simpleforums.models.Comment;
import com.github.sambhavmahajan.simpleforums.models.ForumUser;
import com.github.sambhavmahajan.simpleforums.models.Post;
import com.github.sambhavmahajan.simpleforums.models.Vote;
import com.github.sambhavmahajan.simpleforums.repositories.CommentRepository;
import com.github.sambhavmahajan.simpleforums.repositories.PostRepository;
import com.github.sambhavmahajan.simpleforums.repositories.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VoteService {
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    public VoteService(VoteRepository voteRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
    public List<Vote> getVoteByPostId(long id) {
        return voteRepository.findAllByPostId(id);
    }

    public List<Vote> getVoteByCommentId(long id) {
        return voteRepository.findAllByCommentId(id);
    }

    public List<Vote> getVoteByVoter(ForumUser user) {
        return voteRepository.findAllByVoter(user);
    }
    @Transactional
    public void save(Vote vote) {
        voteRepository.save(vote);
        if(vote.getComment() != null) {
            Comment comment = vote.getComment();
            comment.getVotes().add(vote);
            commentRepository.save(comment);
        } else if(vote.getPost() != null) {
            Post post = vote.getPost();
            post.getVotes().add(vote);
            postRepository.save(post);
        }
    }
    public Vote getVoteByVoterAndComment(ForumUser user, Comment comment) {
        return voteRepository.findByVoter_IdAndComment_Id(user.getId(), comment.getId());
    }
    public Vote getVoteByVoterAndPost(ForumUser user, Post post) {
        return voteRepository.findByVoter_idAndPost_Id(user.getId(), post.getId());
    }
    public void delete(Vote vote) {
        voteRepository.delete(vote);
    }

    public void update(Vote existingVote) {
        voteRepository.save(existingVote);
    }
}
