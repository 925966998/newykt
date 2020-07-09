package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @ClassName RoleEntity
 * @Description: 角色实体类
 * @Author czw
 * @Date 2020/2/19
 **/
public class RoleEntity extends BaseEntity {
    private String roleName;
    private String roleCode;
    private String note;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
