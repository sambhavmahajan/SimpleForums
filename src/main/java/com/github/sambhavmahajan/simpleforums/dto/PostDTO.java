package com.github.sambhavmahajan.simpleforums.dto;

public class PostDTO {
    private String title;
    private String content;
    private String author;
    private String communityName;
    public PostDTO() {}
    public PostDTO(String title, String content, String author, String communityName) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.communityName = communityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
