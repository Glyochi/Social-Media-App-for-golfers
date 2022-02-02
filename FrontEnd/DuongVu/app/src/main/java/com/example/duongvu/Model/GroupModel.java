package com.example.duongvu.Model;

public class GroupModel {
    private int id;
    private boolean joiningRequirePermission;
    private String name;
    private String description;

    public GroupModel() {
    }
    public GroupModel(GroupModel anotherGroup) {
        this.id = anotherGroup.id;
        this.joiningRequirePermission = anotherGroup.joiningRequirePermission;
        this.name = anotherGroup.name;
        this.description = anotherGroup.description;
    }
    public int getId() {
        return id;
    }

    public boolean isJoiningRequirePermission() {
        return joiningRequirePermission;
    }

    public void setJoiningRequirePermission(boolean joiningRequirePermission) {
        this.joiningRequirePermission = joiningRequirePermission;
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
}
