package com.sgms.pojo;

public class Formation {

    private String formId;
    private String formName;
    private String formType;

    public Formation(String formId, String formName, String formType) {
        this.formId = formId;
        this.formName = formName;
        this.formType = formType;
    }


    public String getFormId() {
        return formId;
    }

    public String getFormName() {
        return formName;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
