package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @ClassName ProjectSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
public class ProjectSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id", "projectName", "startTime", "projectCode", "lastTime", "endTime", "totalAmount", "paymentAmount"
                , "surplusAmount", "remark", "projectSourceId", "projectType", "countyAmount", "cityAmount", "provinceAmount", "centerAmount", "reason", "state", "fileId", "operDepartment", "operUser", "type", "documentNum", "paymentDepartment"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,pt.name  as projectTypeName ,ps.projectName as projectSourceName ,d.departmentName,(select SUM(pd.paymentAmount) from project_detail pd WHERE pd.projectId=p.id and pd.state=1) AS paymentAmountResult from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            if(StringUtils.isNotBlank(MapUtils.getString(map, "DJFlag")) && map.get("DJFlag").equals("4J")){
                builder.append(" and p.paymentDepartment = #{departmentId}");
            }else{
                builder.append(GetDepartmentSql.getUserBuilder("p.paymentDepartment"));
            }
        }
        builder.append(" order by p.startTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,pt.name  as projectTypeName ,ps.projectName as projectSourceName ,d.departmentName,(select SUM(pd.paymentAmount) from project_detail pd WHERE pd.projectId=p.id and pd.state=3) AS paymentAmountResult from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.paymentDepartment"));
        }
        builder.append(" order by p.startTime desc");
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
