package com.ky.ykt.mybatis;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Linan
 */
public class BaseEntity implements Serializable {

    /**
     * 基础字段：ID
     */
    private String id;
    /**
     * 基础字段：创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    /**
     * 基础字段：修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date updateTime;
    /**
     * 基础字段：版本号
     */
    private Long version;
    /**
     * 基础字段：逻辑删除标识
     */
    private Integer logicalDel;

    public BaseEntity() {
        this.createTime = new Date();
        this.updateTime = new Date();
        this.logicalDel = 0;
        this.version = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getLogicalDel() {
        return logicalDel;
    }

    public void setLogicalDel(Integer logicalDel) {
        this.logicalDel = logicalDel;
    }
}
