package com.sgms.pojo;

import java.sql.Date;

public class Project {
    private int projectId;
    private String subjectName;
    private String topic;
    private Date dueDate;

    public Project(int projectId, String subjectName, String topic, Date dueDate) {
        this.projectId = projectId;
        this.subjectName = subjectName;
        this.topic = topic;
        this.dueDate = dueDate;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTopic(){
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }




}
