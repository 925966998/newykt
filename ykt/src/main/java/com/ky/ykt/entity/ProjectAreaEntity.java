package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Classname:com.ky.ykt.entity
 * @Auther: ywj
 * @Date: 2020/7/11 10:49
 * @Description:
 */
public class ProjectAreaEntity extends BaseEntity {
    private String projectId;
    private String areaId;
    private BigDecimal areaAmount;
    private String userId;
    private String operDepartment;

    private String projectName;
    private String projectSourceName;
    private String departmentName;
    private String documentNum;
    private String state;
    private String projectTypeName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSourceName() {
        return projectSourceName;
    }

    public void setProjectSourceName(String projectSourceName) {
        this.projectSourceName = projectSourceName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDocumentNum() {
        return documentNum;
    }

    public void setDocumentNum(String documentNum) {
        this.documentNum = documentNum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOperDepartment() {
        return operDepartment;
    }

    public void setOperDepartment(String operDepartment) {
        this.operDepartment = operDepartment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public BigDecimal getAreaAmount() {
        return areaAmount;
    }

    public void setAreaAmount(BigDecimal areaAmount) {
        this.areaAmount = areaAmount;
    }
}
