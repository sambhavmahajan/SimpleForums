package com.github.sambhavmahajan.simpleforums.models;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private long id;
    private String content;
    @ManyToOne
    private Post parentPost;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private ForumUser author;
    private LocalDateTime timeStamp;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Comment> children;
    @OneToMany(cascade = CascadeType.REMOVE)
    private Set<Vote> votes;
    @ManyToOne
    @Nullable
    private Comment parent;
    private long upvotes;
    private long downvotes;

    public Comment(String content, Post parentPost, ForumUser author, LocalDateTime timeStamp, List<Comment> children, Set<Vote> votes, Comment parent, long upvotes, long downvotes) {
        this.content = content;
        this.parentPost = parentPost;
        this.author = author;
        this.timeStamp = timeStamp;
        this.children = children;
        this.votes = votes;
        this.parent = parent;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

    public Comment() {}

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public ForumUser getAuthor() {
        return author;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public List<Comment> getChildren() {
        return children;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public Comment getParent() {
        return parent;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public long getDownvotes() {
        return downvotes;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setParentPost(Post parentPost) {
        this.parentPost = parentPost;
    }

    public void setAuthor(ForumUser author) {
        this.author = author;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setChildren(List<Comment> children) {
        this.children = children;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public void setDownvotes(long downvotes) {
        this.downvotes = downvotes;
    }
}
