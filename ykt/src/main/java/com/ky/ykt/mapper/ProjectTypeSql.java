package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @ClassName DepartmentSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
public class ProjectTypeSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project_type";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"name", "parentId", "projectTypeParent", "note", "type", "policyBasis", "payFlag", "payTarget", "department"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select pt.*,ptp.projectTypeName AS projectTypeParentName,d.departmentName AS departmentName from project_type pt LEFT JOIN project_type_parent ptp ON pt.projectTypeParent=ptp.id LEFT JOIN department d ON pt.department=d.id where 1=1  and  pt.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name = #{name}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select pt.*,ptp.projectTypeName AS projectTypeParentName,d.departmentName AS departmentName from project_type pt LEFT JOIN project_type_parent ptp ON pt.projectTypeParent=ptp.id LEFT JOIN department d ON pt.department=d.id where 1=1  and  pt.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name = #{name}");
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


    public String queryProjectTree(Map map) {
        StringBuilder builder = new StringBuilder("select DISTINCT pt.*,ptp.projectTypeName AS projectTypeParentName from project_type pt LEFT JOIN project_type_parent ptp ON pt.projectTypeParent=ptp.id LEFT JOIN user_projecttype up ON up.projectTypeId=pt.id WHERE pt.logicalDel=0 ");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and up.userId = #{userId}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }
}
