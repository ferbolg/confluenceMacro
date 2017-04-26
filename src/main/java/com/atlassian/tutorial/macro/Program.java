package com.atlassian.tutorial.macro;

/**
 * Created by szi on 4/26/2017.
 */
public class Program {
    private String title = "";
    private String objectives = "";
    private String outcome = "";
    private String owner = "";
    private String deadline = "";
    private String missedDeadline = "";
    private String dependencyAndAction = "";
    private String linkToGoal = "";

    public Program(){};
    public Program(String title, String objectives, String outcome, String owner, String deadline, String missedDeadline, String dependencyAndAction, String linkToGoal) {
        this.title = title;
        this.objectives = objectives;
        this.outcome = outcome;
        this.owner = owner;
        this.deadline = deadline;
        this.missedDeadline = missedDeadline;
        this.dependencyAndAction = dependencyAndAction;
        this.linkToGoal = linkToGoal;
    }

    @Override
    public String toString() {
        return "Program{" +
                "title='" + title + '\'' +
                ", objectives='" + objectives + '\'' +
                ", outcome='" + outcome + '\'' +
                ", owner='" + owner + '\'' +
                ", deadline='" + deadline + '\'' +
                ", missedDeadline='" + missedDeadline + '\'' +
                ", dependencyAndAction='" + dependencyAndAction + '\'' +
                ", linkToGoal='" + linkToGoal + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getMissedDeadline() {
        return missedDeadline;
    }

    public void setMissedDeadline(String missedDeadline) {
        this.missedDeadline = missedDeadline;
    }

    public String getDependencyAndAction() {
        return dependencyAndAction;
    }

    public void setDependencyAndAction(String dependencyAndAction) {
        this.dependencyAndAction = dependencyAndAction;
    }

    public String getLinkToGoal() {
        return linkToGoal;
    }

    public void setLinkToGoal(String linkToGoal) {
        this.linkToGoal = linkToGoal;
    }
}
