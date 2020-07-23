package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-27 16:42
 * @version: v1.0
 */
public class ProjectAreaSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "project_area";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"projectId", "areaId", "areaAmount","userId","operDepartment"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,CONCAT_WS('-',pt.name,p.batchNumber) as projectTypeName,ps.projectName as projectSourceName ,d.departmentName,pa.areaAmount from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId LEFT JOIN project_area pa ON pa.projectId = p.id LEFT JOIN user_projecttype upt ON p.projectName = upt.projectTypeId where 1=1 and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pa.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "areaId"))) {
            builder.append(" and pa.areaId =#{areaId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "areaAmount"))) {
            builder.append(" and pa.areaAmount =#{areaAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and pa.userId =#{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "proUserId"))) {
            builder.append(" and upt.userId =#{proUserId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operDepartment"))) {
            builder.append(" and pa.operDepartment =#{operDepartment}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,CONCAT_WS('-',pt.name,p.batchNumber) as projectTypeName,ps.projectName as projectSourceName ,d.departmentName,pa.areaAmount from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId LEFT JOIN project_area pa ON pa.projectId = p.id LEFT JOIN user_projecttype upt ON p.projectName = upt.projectTypeId where 1=1 and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pa.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "areaId"))) {
            builder.append(" and pa.areaId =#{areaId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "areaAmount"))) {
            builder.append(" and pa.areaAmount =#{areaAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and pa.userId =#{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "proUserId"))) {
            builder.append(" and upt.userId =#{proUserId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operDepartment"))) {
            builder.append(" and pa.operDepartment =#{operDepartment}");
        }
        builder.append(" order by createTime desc");
        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }

    public StringBuilder pageHelp(long currentPage, long pageSize) {
        long count = (currentPage - 1) * pageSize;
        if (count != 0) {
            count = count;
        }
        StringBuilder builder = new StringBuilder(" limit ");
        builder.append(count);
        builder.append(" ,");
        builder.append(pageSize);
        return builder;
    }
}