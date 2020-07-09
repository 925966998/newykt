package com.ky.ykt.entity;

import com.ky.ykt.mybatis.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName ProjectDetail
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
public class ProjectReplacementEntity extends BaseEntity {
    private String projectDetailId;
    private String projectId;

    public String getProjectDetailId() {
        return projectDetailId;
    }

    public void setProjectDetailId(String projectDetailId) {
        this.projectDetailId = projectDetailId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
