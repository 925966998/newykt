package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.util.List;

/**
 * @ClassName DepartmentEntity
 * @Description: 部门实体类
 * @Author czw
 * @Date 2020/2/18
 **/
public class DepartmentEntity extends BaseEntity {

    private String departmentName;

    private String departmentNum;
    private String parentId;

    private Integer isUse;

    private String note;
    private String areaId;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    private List<DepartmentEntity> children;

    public List<DepartmentEntity> getChildren() {
        return children;
    }

    public void setChildren(List<DepartmentEntity> children) {
        this.children = children;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentNum() {
        return departmentNum;
    }

    public void setDepartmentNum(String departmentNum) {
        this.departmentNum = departmentNum;
    }

    public Integer getIsUse() {
        return isUse;
    }

    public void setIsUse(Integer isUse) {
        this.isUse = isUse;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
