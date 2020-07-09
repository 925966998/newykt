package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

/**
 * @ClassName UserLogEntity
 * @Description: 用户操作记录实体类
 * @Author czw
 * @Date 2020/2/18
 **/
public class UserLogEntity extends BaseEntity {

    private String content;

    private String description;

    private String ip;

    private String module;

    private String userName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
