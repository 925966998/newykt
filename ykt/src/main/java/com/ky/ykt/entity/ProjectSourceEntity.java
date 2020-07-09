package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectSource
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/5
 **/
public class ProjectSourceEntity extends BaseEntity {
    private String department;
    private String projectName;
    private String documentNum;
    private String projectType;
    private String operDepartment;
    private BigDecimal totalAmount;
    private BigDecimal paymentAmount;
    private BigDecimal surplusAmount;
    private BigDecimal centerAmount;
    private BigDecimal provinceAmount;
    private BigDecimal cityAmount;
    private BigDecimal countyAmount;
    private String note;
    private String projectSourceName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;
    private String projectTypeName;
    private String departmentName;
    private String fileId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getOperDepartment() {
        return operDepartment;
    }

    public void setOperDepartment(String operDepartment) {
        this.operDepartment = operDepartment;
    }

    public String getProjectSourceName() {
        return projectSourceName;
    }

    public void setProjectSourceName(String projectSourceName) {
        this.projectSourceName = projectSourceName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(String documentNum) {
        this.documentNum = documentNum;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
