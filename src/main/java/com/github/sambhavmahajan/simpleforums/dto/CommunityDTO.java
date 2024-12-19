package com.github.sambhavmahajan.simpleforums.dto;

import java.util.ArrayList;
import java.util.List;

public class CommunityDTO {
    private String name;
    private String description;
    private String rules;
    public CommunityDTO() {}
    public CommunityDTO(String name, String description, String rules) {
        this.name = name;
        this.description = description;
        this.rules = rules;
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
    public String getRules() {
        return rules;
    }
    public void setRules(String rules) {
        this.rules = rules;
    }
}
