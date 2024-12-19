package com.github.sambhavmahajan.simpleforums.models;

import jakarta.persistence.*;

@Entity
public class Vote {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    private ForumUser voter;
    private boolean upvote;
    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = true)
    private Comment comment;
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    public Vote(ForumUser voter, boolean upvote, Comment comment, Post post) {
        this.voter = voter;
        this.upvote = upvote;
        this.comment = comment;
        this.post = post;
    }
    public Vote(){}

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ForumUser getVoter() {
        return voter;
    }

    public void setVoter(ForumUser voter) {
        this.voter = voter;
    }

    public boolean isUpvote() {
        return upvote;
    }

    public void setUpvote(boolean upvote) {
        this.upvote = upvote;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}


