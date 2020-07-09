package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-09 18:03
 * @version: v1.0
 */
public class PersonReplacementEntity extends BaseEntity {
    private String personId;
    private String replacementAmount;
    private String departmentId;
    private String userId;
    private String name;
    private String perId;
    private String phone;
    private String bankCardNo;
    private String idCardNo;
    private String projectId;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPerId() {
        return perId;
    }

    public void setPerId(String perId) {
        this.perId = perId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getReplacementAmount() {
        return replacementAmount;
    }

    public void setReplacementAmount(String replacementAmount) {
        this.replacementAmount = replacementAmount;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PersonReplacementEntity{" +
                "personId='" + personId + '\'' +
                ", replacementAmount='" + replacementAmount + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}