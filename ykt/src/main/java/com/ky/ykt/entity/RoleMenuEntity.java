package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

public class RoleMenuEntity extends BaseEntity {
    private String roleId;
    private String menuId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
