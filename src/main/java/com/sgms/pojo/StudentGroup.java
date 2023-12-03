package com.sgms.pojo;



public class StudentGroup {
    private Integer groupListId;

    private Integer groupId;

    private String projectName;

    private String student1Name;

    private String student2Name;

    private Integer projectGrade;

    public StudentGroup(int groupListId, int groupId, String projectName, String student1Name, String student2Name, int projectGrade) {
        this.groupListId = groupListId;
        this.groupId = groupId;
        this.projectName = projectName;
        this.student1Name = student1Name;
        this.student2Name = student2Name;
        this.projectGrade = projectGrade;
    }

    public Integer getGroupListId(){return groupListId;}
    public Integer getGroupId() {
        return groupId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getStudent1Name(){return student1Name;}

    public String getStudent2Name(){return student2Name;}

    public Integer getProjectGrade(){return projectGrade;}


    public void setGroupListId(int groupListId){this.groupListId = groupListId;}

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setStudent1Name(String student1Name){this.student1Name = student1Name;}

    public void setStudent2Name(String student2Name){this.student2Name = student2Name;}

    public void setProjectGrade(int projectGrade){this.projectGrade = projectGrade;}


}