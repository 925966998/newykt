package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @Classname:com.ky.ykt.entity
 * @Auther: ywj
 * @Date: 2020/7/15 17:58
 * @Description:
 */
public class UserProjectTypeEntity extends BaseEntity {
    private String userId;
    private String projectTypeId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }
}
