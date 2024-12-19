package com.github.sambhavmahajan.simpleforums.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String description;
    private long memberCount;
    @ManyToMany
    private Set<ForumUser> members;
    @ManyToOne
    private ForumUser owner;
    @OneToMany
    private Set<Post> posts;
    @ElementCollection
    private List<String> rules;

    public Community(String name, String description, long memberCount, Set<ForumUser> members, ForumUser owner, Set<Post> posts, ArrayList<String> rules) {
        this.name = name;
        this.description = description;
        this.memberCount = memberCount;
        this.members = members;
        this.owner = owner;
        this.posts = posts;
        this.rules = rules;
    }
    public Community() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(long memberCount) {
        this.memberCount = memberCount;
    }

    public Set<ForumUser> getMembers() {
        return members;
    }

    public void setMembers(Set<ForumUser> members) {
        this.members = members;
    }

    public ForumUser getOwner() {
        return owner;
    }

    public void setOwner(ForumUser owner) {
        this.owner = owner;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }
    public List<String> getRules() {
        return rules;
    }
    public void setRules(ArrayList<String> rules) {
        this.rules = rules;
    }
    public void setRulesFromString(String rules) {
        String[] li = rules.split("\n");
        this.rules = new ArrayList<>();
        for(int i = 0; i < li.length; i++) {
            this.rules.add(li[i]);
        }
    }
    public String getUrl() {
        return "/community/" + this.name;
    }
}
