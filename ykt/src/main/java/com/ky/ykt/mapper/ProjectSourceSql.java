package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @ClassName DepartmentSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
public class ProjectSourceSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project_source";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"projectType", "documentNum", "operDepartment", "countyAmount", "cityAmount", "provinceAmount", "centerAmount", "totalAmount", "paymentAmount"
                , "surplusAmount", "department", "startTime", "projectName", "note","fileId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select ps.*,pt.name  as projectTypeName ,d.departmentName from project_source ps LEFT JOIN department d ON ps.department =d.id LEFT JOIN project_type pt ON pt.id=ps.projectType where 1=1  and ps.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and ps.projectName = #{projectName}");
        }
        builder.append(GetDepartmentSql.getUserBuilder("ps.operDepartment"));
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select ps.*,pt.name  as projectTypeName ,d.departmentName from project_source ps LEFT JOIN department d ON ps.department =d.id LEFT JOIN project_type pt ON pt.id=ps.projectType where 1=1  and ps.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and ps.projectName = #{projectName}");
        }
        builder.append(GetDepartmentSql.getUserBuilder("ps.operDepartment"));
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
