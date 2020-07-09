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
public class ProjectTypeParentSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project_type_parent";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"projectTypeName", "note", "projectTypeCode", "department"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select pt.*,d.departmentName from " + this.getTableName() + " pt LEFT JOIN department d ON pt.department=d.id  where 1=1  and pt.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectTypeName"))) {
            builder.append(" and projectTypeName = #{projectTypeName}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select pt.*,d.departmentName from " + this.getTableName() + " pt LEFT JOIN department d ON pt.department=d.id  where 1=1  and pt.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectTypeName"))) {
            builder.append(" and projectTypeName = #{projectTypeName}");
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
