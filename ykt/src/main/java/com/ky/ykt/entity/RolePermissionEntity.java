package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:15
 * @version: v1.0
 */
public class RolePermissionEntity extends BaseEntity {
    private String id;
    private String roleId;
    private String permissionId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public RolePermissionEntity() {
    }

    public RolePermissionEntity(String id, String roleId, String permissionId) {
        this.id = id;
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}