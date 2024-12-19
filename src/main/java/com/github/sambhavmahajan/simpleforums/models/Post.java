package com.github.sambhavmahajan.simpleforums.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String content;
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private ForumUser author;
    private LocalDateTime timeStamp;
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();
    @OneToMany
    private Set<Vote> votes = new HashSet<>();
    @ManyToOne
    private Community community;
    private long upvotes;
    private long downvotes;

    public Post(String title, String content, ForumUser author, LocalDateTime timeStamp, ArrayList<Comment> comments, HashSet<Vote> votes, Community community, long upvotes, long downvotes) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.timeStamp = timeStamp;
        this.comments = comments;
        this.votes = votes;
        this.community = community;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
    public Post() {}
    public long getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(long downvotes) {
        this.downvotes = downvotes;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(HashSet<Vote> votes) {
        this.votes = votes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ForumUser getAuthor() {
        return author;
    }

    public void setAuthor(ForumUser author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return "/community/" + community.getName() + "/" + id;
    }
}
