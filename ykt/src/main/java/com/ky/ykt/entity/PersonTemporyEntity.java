package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-11 10:01
 * @version: v1.0
 */
public class PersonTemporyEntity extends BaseEntity {
    private String name;
    private String phone;
    private String idCardNo;
    private String projectId;
    private String departmentId;
    private String county;
    private String address;
    private String bankCardNo;
    private String grantAmount;
    private Integer status;
    private String personId;
    private String departmentName;
    private String projectName;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getGrantAmount() {
        return grantAmount;
    }

    public void setGrantAmount(String grantAmount) {
        this.grantAmount = grantAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PersonTemporyEntity() {
    }

    public PersonTemporyEntity(String name, String phone, String idCardNo, String projectId, String departmentId, String county, String address, String bankCardNo, String grantAmount, Integer status, String personId, String departmentName, String projectName, String userId) {
        this.name = name;
        this.phone = phone;
        this.idCardNo = idCardNo;
        this.projectId = projectId;
        this.departmentId = departmentId;
        this.county = county;
        this.address = address;
        this.bankCardNo = bankCardNo;
        this.grantAmount = grantAmount;
        this.status = status;
        this.personId = personId;
        this.departmentName = departmentName;
        this.projectName = projectName;
        this.userId = userId;
    }
}