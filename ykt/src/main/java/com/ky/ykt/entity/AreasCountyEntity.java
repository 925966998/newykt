package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-29 8:18
 * @version: v1.0
 */
public class AreasCountyEntity extends BaseEntity {
    private String cname;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public AreasCountyEntity(String cname) {
        this.cname = cname;
    }

    public AreasCountyEntity() {
    }
}