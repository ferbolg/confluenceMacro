package com.atlassian.tutorial.macro;

/**
 * Created by szi on 4/24/2017.
 */
public class Project {
    private String owner;
    private String deadline;
    private String title;

    public Project(String owner, String deadline, String title) {
        this.owner = owner;
        this.deadline = deadline;
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getTitle() {
        return title;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Project{" +
                "owner='" + owner + '\'' +
                ", deadline='" + deadline + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
