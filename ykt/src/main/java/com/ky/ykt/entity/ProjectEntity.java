package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectEntity
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/26
 **/
public class ProjectEntity extends BaseEntity {
    private String projectName;
    private String ProjectCode;
    private String projectSourceName;
    private String projectSourceId;
    private BigDecimal paymentAmountResult;
    private String projectType;
    private BigDecimal centerAmount;
    private BigDecimal provinceAmount;
    private BigDecimal cityAmount;
    private BigDecimal countyAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lastTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private BigDecimal surplusAmount;
    private String remark;
    private String reason;
    private String operDepartment;
    private String operUser;
    private String fileId;
    private Integer state;
    private Integer type;
    private String documentNum;
    private String paymentDepartment;
    private String departmentName;
    private String projectTypeName;
    private Integer batchNumber;

    public Integer getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(Integer batchNumber) {
        this.batchNumber = batchNumber;
    }

    public String getProjectSourceId() {
        return projectSourceId;
    }

    public void setProjectSourceId(String projectSourceId) {
        this.projectSourceId = projectSourceId;
    }

    public String getProjectSourceName() {
        return projectSourceName;
    }

    public void setProjectSourceName(String projectSourceName) {
        this.projectSourceName = projectSourceName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public BigDecimal getCenterAmount() {
        return centerAmount;
    }

    public void setCenterAmount(BigDecimal centerAmount) {
        this.centerAmount = centerAmount;
    }

    public BigDecimal getProvinceAmount() {
        return provinceAmount;
    }

    public void setProvinceAmount(BigDecimal provinceAmount) {
        this.provinceAmount = provinceAmount;
    }

    public BigDecimal getCityAmount() {
        return cityAmount;
    }

    public void setCityAmount(BigDecimal cityAmount) {
        this.cityAmount = cityAmount;
    }

    public BigDecimal getCountyAmount() {
        return countyAmount;
    }

    public void setCountyAmount(BigDecimal countyAmount) {
        this.countyAmount = countyAmount;
    }

    public BigDecimal getPaymentAmountResult() {
        return paymentAmountResult;
    }

    public void setPaymentAmountResult(BigDecimal paymentAmountResult) {
        this.paymentAmountResult = paymentAmountResult;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(String documentNum) {
        this.documentNum = documentNum;
    }

    public String getPaymentDepartment() {
        return paymentDepartment;
    }

    public void setPaymentDepartment(String paymentDepartment) {
        this.paymentDepartment = paymentDepartment;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getProjectCode() {
        return ProjectCode;
    }

    public void setProjectCode(String projectCode) {
        ProjectCode = projectCode;
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

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
