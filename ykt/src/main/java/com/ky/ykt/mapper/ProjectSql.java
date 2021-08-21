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
                , "surplusAmount", "remark", "projectSourceId", "projectType", "countyAmount", "cityAmount", "provinceAmount", "centerAmount", "reason", "state", "fileId", "operDepartment", "operUser", "type", "documentNum", "paymentDepartment","batchNumber"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,CONCAT_WS('-',pt.name,p.batchNumber) as projectTypeName,ps.projectName as projectSourceName ,d.departmentName,(select SUM(pd.paymentAmount) from project_detail pd WHERE pd.projectId=p.id and pd.state=1) AS paymentAmountResult from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId ");
        if(StringUtils.isNotBlank(MapUtils.getString(map, "DJFlag")) && map.get("DJFlag").equals("4J")){
            builder.append(" left join user_projecttype upt on p.projectName = upt.projectTypeId");
        }
        builder.append(" where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "batchNumber"))) {
            builder.append(" and p.batchNumber = #{batchNumber}");
        }
        if(StringUtils.isNotBlank(MapUtils.getString(map, "userId"))){
            builder.append(" and upt.userId = #{userId}");
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
        StringBuilder builder = new StringBuilder("select p.*,CONCAT_WS('-',pt.name,p.batchNumber)  as projectTypeName ,ps.projectName as projectSourceName ,d.departmentName,(select SUM(pd.paymentAmount) from project_detail pd WHERE pd.projectId=p.id and pd.state=3) AS paymentAmountResult from project p LEFT JOIN department d ON p.paymentDepartment =d.id LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN project_source ps ON ps.id=p.projectSourceId");
        if(StringUtils.isNotBlank(MapUtils.getString(map, "DJFlag")) && map.get("DJFlag").equals("4J")){
            builder.append(" left join user_projecttype upt on p.projectName = upt.projectTypeId");
        }
        builder.append(" where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "batchNumber"))) {
            builder.append(" and p.batchNumber = #{batchNumber}");
        }
        if(StringUtils.isNotBlank(MapUtils.getString(map, "userId"))){
            builder.append(" and upt.userId = #{userId}");
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

    public String queryProject(Map map) {
        StringBuilder builder = new StringBuilder("select p.*,CONCAT_WS('-',pt.name,p.batchNumber) as projectTypeName from project p LEFT JOIN project_type pt ON pt.id=p.projectType LEFT JOIN user_projecttype up ON up.projectTypeId= p.projectType ");
        builder.append(" where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "batchNumber"))) {
            builder.append(" and p.batchNumber = #{batchNumber}");
        }
        if(StringUtils.isNotBlank(MapUtils.getString(map, "userId"))){
            builder.append(" and up.userId = #{userId}");
        }
        builder.append(" order by p.startTime desc");
        return builder.toString();
    }

    public String queryFFproject(Map map) {
    StringBuilder builder =
        new StringBuilder(
            "SELECT p.*,CONCAT_WS( '-', pt.NAME, p.batchNumber ) AS projectTypeName,(\n"
                + "\tSELECT\n"
                + "\t\tSUM( pd.paymentAmount ) \n"
                + "\tFROM\n"
                + "\t\tproject_detail pd \n"
                + "\tWHERE\n"
                + "\t\tpd.projectId = p.id \n"
                + "\t\tAND pd.state = 1 \n"
                + "\t) AS paymentAmountResult FROM project_detail pd LEFT JOIN project p on pd.projectId = p.id LEFT JOIN project_type pt on pt.id = p.projectType  ");
        if(StringUtils.isNotBlank(MapUtils.getString(map, "DJFlag")) && map.get("DJFlag").equals("4J")){
            builder.append(" left join user_projecttype upt on p.projectName = upt.projectTypeId");
        }
        builder.append(" where 1=1  and p.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and p.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "batchNumber"))) {
            builder.append(" and p.batchNumber = #{batchNumber}");
        }
        if(StringUtils.isNotBlank(MapUtils.getString(map, "userId"))){
            builder.append(" and upt.userId = #{userId}");
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

    public String queryType(Map map) {
        StringBuilder builder =
                new StringBuilder(
                        "select pt.`name` as projectType,COUNT(*) as num,p.paymentDepartment from project p LEFT JOIN project_type pt ON p.projectType=pt.id LEFT JOIN user_projecttype upt ON upt.projectTypeId = pt.id where 1=1 ");
        if(StringUtils.isNotBlank(MapUtils.getString(map, "userId"))){
            builder.append(" and upt.userId = #{userId}");
        }
        builder.append(" GROUP BY projectType");
        return builder.toString();
    }
}
