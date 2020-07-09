package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @ClassName ProjectTypeEntity
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/4
 **/
public class ProjectTypeEntity extends BaseEntity {
    private String name;
    private String note;
    private String parentId;
    private Integer type;
    private String policyBasis;
    private String payFlag;
    private String payTarget;
    private String department;
    private String projectTypeParentName;
    private String departmentName;
    private String projectTypeParent;

    public String getProjectTypeParent() {
        return projectTypeParent;
    }

    public void setProjectTypeParent(String projectTypeParent) {
        this.projectTypeParent = projectTypeParent;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProjectTypeParentName() {
        return projectTypeParentName;
    }

    public void setProjectTypeParentName(String projectTypeParentName) {
        this.projectTypeParentName = projectTypeParentName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPolicyBasis() {
        return policyBasis;
    }

    public void setPolicyBasis(String policyBasis) {
        this.policyBasis = policyBasis;
    }

    public String getPayFlag() {
        return payFlag;
    }

    public void setPayFlag(String payFlag) {
        this.payFlag = payFlag;
    }

    public String getPayTarget() {
        return payTarget;
    }

    public void setPayTarget(String payTarget) {
        this.payTarget = payTarget;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
