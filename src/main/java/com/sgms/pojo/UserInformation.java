package com.sgms.pojo;

public class UserInformation {
    private String name;
    private String id;
    private String job;
    private String password;
    private String newPassword;


    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }



    private final static UserInformation USER_INFORMATION = new UserInformation();

    //    public UserInformation(String id,String name,String formation,String promotion) {
//        this.name = name;
//        this.id = id;
//    }
    private UserInformation() {
    }

    public static UserInformation getUser() {
        return USER_INFORMATION;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

}
