package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectDetail
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
public class ProjectDetailEntity extends BaseEntity {
    private String projectId;
    private String parentId;
    private String projectName;
    private Date startTime;
    private Date lastTime;
    private Date endTime;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private BigDecimal surplusAmount;
    private String remark;
    private String reason;
    private String paymentDepartment;
    private String operDepartment;
    private String operDepartmentChildren;
    private Integer state;
    private String operUser;
    private String departmentName;
    private String departmentNames;
    private String projectTypeName;

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getDepartmentNames() {
        return departmentNames;
    }

    public void setDepartmentNames(String departmentNames) {
        this.departmentNames = departmentNames;
    }

    public String getOperDepartmentChildren() {
        return operDepartmentChildren;
    }

    public void setOperDepartmentChildren(String operDepartmentChildren) {
        this.operDepartmentChildren = operDepartmentChildren;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(BigDecimal surplusAmount) {
        this.surplusAmount = surplusAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPaymentDepartment() {
        return paymentDepartment;
    }

    public void setPaymentDepartment(String paymentDepartment) {
        this.paymentDepartment = paymentDepartment;
    }

    public String getOperDepartment() {
        return operDepartment;
    }

    public void setOperDepartment(String operDepartment) {
        this.operDepartment = operDepartment;
    }

    public String getOperUser() {
        return operUser;
    }

    public void setOperUser(String operUser) {
        this.operUser = operUser;
    }
}
