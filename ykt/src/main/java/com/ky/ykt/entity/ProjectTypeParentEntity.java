package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @ClassName ProjectTypeParent
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/9
 **/
public class ProjectTypeParentEntity extends BaseEntity {
    private String projectTypeName;
    private String projectTypeCode;
    private String department;
    private String note;
    private String departmentName;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectTypeCode() {
        return projectTypeCode;
    }

    public void setProjectTypeCode(String projectTypeCode) {
        this.projectTypeCode = projectTypeCode;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
