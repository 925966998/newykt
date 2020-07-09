package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: ykt
 * @classDesc: 功能描述（区域划分）
 * @author: yaoWieJie
 * @createTime: 2020-02-27 16:35
 * @version: v1.0
 */
public class AreasEntity extends BaseEntity {

    private String code;
    private String name;
    private Integer level;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    private String parentId;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}