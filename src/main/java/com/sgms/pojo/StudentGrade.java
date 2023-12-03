package com.sgms.pojo;


import java.sql.Date;

public class StudentGrade {
    private String name;
    private Date date;
    private String java;
    private String sar;
    private String marketing;
    private String ml;
    private Integer id;
    private String fid;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StudentGrade(Date date, String fid, String java, String sar, String marketing, String ml, String name, Integer id) {
        this.date = date;
        this.java = java;
        this.sar = sar;
        this.marketing = marketing;
        this.ml = ml;
        this.name = name;
        this.id = id;
        this.fid = fid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public String getFid() {
        return fid;
    }


    public String getJava() {
        return java;
    }

    public String getSar() {
        return sar;
    }

    public String getMarketing() {
        return marketing;
    }

    public String getMl() {
        return ml;
    }


    public void setDate(String date) {
        this.date = Date.valueOf(date);
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public void setJava(String java) {
        this.java = java;
    }

    public void setSar(String sar) {
        this.sar = sar;
    }

    public void setMarketing(String marketing) {
        this.marketing = marketing;
    }

    public void setMl(String ml) {
        this.ml = ml;
    }


    @Override
    public String toString() {
        return "StudentGrade{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", fid=" + fid +
                ", java='" + java + '\'' +
                ", sar='" + sar + '\'' +
                ", marketing='" + marketing + '\'' +
                ", ml='" + ml + '\'' +
                ", id=" + id +
                '}';
    }
}