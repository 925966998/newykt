package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:13
 * @version: v1.0
 */
public class UserRoleEntity extends BaseEntity {
    private String id;
    private String userId;
    private String roleId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public UserRoleEntity() {
    }

    public UserRoleEntity(String id, String userId, String roleId) {
        this.id = id;
        this.userId = userId;
        this.roleId = roleId;
    }

}