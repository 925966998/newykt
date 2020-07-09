package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.util.List;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:05
 * @version: v1.0
 */
public class PermissionEntity extends BaseEntity {

    private String id;
    private Integer parentId;
    private String perName;
    private String perUrl;
    private String perPerms;
    private String perIcon;
    private Integer perType;
    private Integer perSort;
    private List<PermissionEntity> chidren;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public String getPerUrl() {
        return perUrl;
    }

    public void setPerUrl(String perUrl) {
        this.perUrl = perUrl;
    }

    public String getPerPerms() {
        return perPerms;
    }

    public void setPerPerms(String perPerms) {
        this.perPerms = perPerms;
    }

    public String getPerIcon() {
        return perIcon;
    }

    public void setPerIcon(String perIcon) {
        this.perIcon = perIcon;
    }

    public Integer getPerType() {
        return perType;
    }

    public void setPerType(Integer perType) {
        this.perType = perType;
    }

    public Integer getPerSort() {
        return perSort;
    }

    public void setPerSort(Integer perSort) {
        this.perSort = perSort;
    }

    public List<PermissionEntity> getChidren() {
        return chidren;
    }

    public void setChidren(List<PermissionEntity> chidren) {
        this.chidren = chidren;
    }

    public PermissionEntity() {
    }

    public PermissionEntity(String id, Integer parentId, String perName, String perUrl, String perPerms, String perIcon, Integer perType, Integer perSort, List<PermissionEntity> chidren) {
        this.id = id;
        this.parentId = parentId;
        this.perName = perName;
        this.perUrl = perUrl;
        this.perPerms = perPerms;
        this.perIcon = perIcon;
        this.perType = perType;
        this.perSort = perSort;
        this.chidren = chidren;
    }
}