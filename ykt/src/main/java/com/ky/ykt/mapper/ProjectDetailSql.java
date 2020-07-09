package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectDetailSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
public class ProjectDetailSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project_detail";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"projectId", "projectName", "startTime", "lastTime", "endTime", "totalAmount", "paymentAmount"
                , "surplusAmount", "remark", "state", "fileId", "reason", "parentId", "paymentDepartment", "operDepartment", "operUser"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select pd.*,d.departmentName as departmentName,dt.departmentName as departmentNames,pt.name as projectTypeName from project_detail pd LEFT JOIN department d ON d.id=pd.paymentDepartment LEFT JOIN  project p ON pd.projectId=p.id LEFT JOIN  department dt ON pd.operDepartment=dt.id LEFT JOIN  project_type pt ON pd.projectName=pt.id  where 1=1  and pd.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pd.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operDepartment"))) {
            builder.append(" and pd.operDepartment = #{operDepartment}");
        }
        /*
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and pd.state = #{state}");
        }
        */
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and pd.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operUser"))) {
            builder.append(" and pd.operUser = #{operUser}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "paymentDepartment"))) {
            builder.append(" and pd.paymentDepartment = #{paymentDepartment}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("pd.operDepartment"));
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("0")) {
            builder.append(" and pd.state = 0 ");
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("1")) {
            builder.append(" and pd.state != 0");
        }else if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("3")) {
            builder.append(" and pd.state = 1");
        }
        builder.append(" order by pd.startTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select pd.*,d.departmentName as departmentName,dt.departmentName as departmentNames,pt.name as projectTypeName from project_detail pd LEFT JOIN department d ON d.id=pd.paymentDepartment LEFT JOIN  project p ON pd.projectId=p.id LEFT JOIN  department dt ON pd.operDepartment=dt.id LEFT JOIN  project_type pt ON pd.projectName=pt.id where 1=1  and pd.logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pd.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operDepartment"))) {
            builder.append(" and pd.operDepartment = #{operDepartment}");
        }
        /*
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and pd.state = #{state}");
        }
        */
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectName"))) {
            builder.append(" and pd.projectName = #{projectName}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "operUser"))) {
            builder.append(" and pd.operUser = #{operUser}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "paymentDepartment"))) {
            builder.append(" and pd.paymentDepartment = #{paymentDepartment}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdListFlag")) && map.get("departmentIdListFlag").equals("departmentIdListFlag")) {
                if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdList"))) {
                    builder.append(" and pd.operDepartment in (");
                    if (map.get("departmentIdList") instanceof List) {
                        List<String> departmentIdList = (List) map.get("departmentIdList");
                        for (String id : departmentIdList) {
                            if (departmentIdList.indexOf(id) > 0)
                                builder.append(",");
                            builder.append("'").append(id).append("'");
                        }
                    } else {
                        builder.append(map.get("departmentIdList"));
                    }
                    builder.append(")");
                }
            } else {
                if(StringUtils.isNotBlank(MapUtils.getString(map, "operDepartment"))){
                    builder.append(" and pd.operDepartment = #{operDepartment}");

                }
            }
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("0")) {
            builder.append(" and pd.state = 0 ");
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("1")) {
            builder.append(" and pd.state != 0");
        }else if (StringUtils.isNotBlank(MapUtils.getString(map, "projectStatus")) && map.get("projectStatus").equals("3")) {
            builder.append(" and pd.state = 1");
        }
        builder.append(" order by pd.startTime desc");
        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }

    public String statisticPage(Map map) {
        StringBuilder builder = new StringBuilder("SELECT\n" +
                "pt.name AS projectName,\n" +
                "pd.paymentAmount AS paymentAmount,\n" +
                "d.departmentName AS departmentName,\n" +
                "pd.startTime AS startTime,\n" +
                "pd.totalAmount AS totalAmount,\n" +
                "pt.`name` AS projectTypeName\n" +
                "\n" +
                "FROM\n" +
                "project_detail pd\n" +
                "LEFT JOIN department d ON d.id = pd.paymentDepartment\n" +
                "LEFT JOIN project p ON p.id=pd.projectId\n" +
                "LEFT JOIN project_type pt ON p.projectType = pt.id\n" +
                "WHERE\n" +
                "1 = 1\n" +
                "AND pd.logicalDel = 0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectType"))) {
            builder.append(" and p.projectType = #{projectType}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "paymentDepartment"))) {
            builder.append(" and pd.paymentDepartment = #{paymentDepartment}");
        }
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isBlank(MapUtils.getString(map, "startTime")))
            startTime = dealStartEndDate(map.get("startTime").toString(), "startTime");
        if (!StringUtils.isBlank(MapUtils.getString(map, "endTime")))
            endTime = dealStartEndDate(map.get("endTime").toString(), "endTime");
        if (!startTime.equals("") || !endTime.equals("")) {
            builder.append(" and  pd.startTime between '" + startTime + "' and '" + endTime + "' ");
        }
        builder.append(" order by pd.startTime desc");
        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }

    public String statisticCount(Map map) {
        StringBuilder builder = new StringBuilder("SELECT sum(pd.paymentAmount)" +
                "\n" +
                "FROM\n" +
                "project_detail pd\n" +
                "LEFT JOIN department d ON d.id = pd.paymentDepartment\n" +
                "LEFT JOIN project p ON p.id=pd.projectId\n" +
                "LEFT JOIN project_type pt ON p.projectType = pt.id\n" +
                "WHERE\n" +
                "1 = 1\n" +
                "AND pd.logicalDel = 0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectType"))) {
            builder.append(" and p.projectType = #{projectType}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "paymentDepartment"))) {
            builder.append(" and pd.paymentDepartment = #{paymentDepartment}");
        }
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isBlank(MapUtils.getString(map, "startTime")))
            startTime = dealStartEndDate(map.get("startTime").toString(), "startTime");
        if (!StringUtils.isBlank(MapUtils.getString(map, "endTime")))
            endTime = dealStartEndDate(map.get("endTime").toString(), "endTime");
        if (!startTime.equals("") || !endTime.equals("")) {
            builder.append(" and  pd.startTime between '" + startTime + "' and '" + endTime + "' ");
        }
        return builder.toString();
    }

    public String statisticPageCount(Map map) {
        StringBuilder builder = new StringBuilder("SELECT count(*)" +
                "FROM\n" +
                "project_detail pd\n" +
                "LEFT JOIN department d ON d.id = pd.paymentDepartment\n" +
                "LEFT JOIN project p ON p.id=pd.projectId\n" +
                "LEFT JOIN project_type pt ON p.projectType = pt.id\n" +
                "WHERE\n" +
                "1 = 1\n" +
                "AND pd.logicalDel = 0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectType"))) {
            builder.append(" and p.projectType = #{projectType}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "paymentDepartment"))) {
            builder.append(" and pd.paymentDepartment = #{paymentDepartment}");
        }
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isBlank(MapUtils.getString(map, "startTime")))
            startTime = dealStartEndDate(map.get("startTime").toString(), "startTime");
        if (!StringUtils.isBlank(MapUtils.getString(map, "endTime")))
            endTime = dealStartEndDate(map.get("endTime").toString(), "endTime");
        if (!startTime.equals("") || !endTime.equals("")) {
            builder.append(" and  pd.startTime between '" + startTime + "' and '" + endTime + "' ");
        }
        builder.append(" order by pd.startTime desc");
        return builder.toString();
    }

    private String dealStartEndDate(String date, String type) {
        if (type.contains("start"))
            return date + " 00:00:00";
        if (type.contains("end"))
            return date + " 23:59:59";
        return date;
    }

    public String statistic(Map map) {
        StringBuilder builder = new StringBuilder("SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT( " +
                "(CURDATE() - INTERVAL 5 MONTH), " +
                "'%Y-%m' " +
                ") AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT( " +
                "CURDATE() - INTERVAL 5 MONTH, " +
                "'%Y-%m' " +
                ") " +
                "UNION " +
                "SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT( " +
                "(CURDATE() - INTERVAL 4 MONTH), " +
                "'%Y-%m' " +
                ") AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT( " +
                "CURDATE() - INTERVAL 4 MONTH, " +
                "'%Y-%m' " +
                ") " +
                "UNION " +
                "SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT( " +
                "(CURDATE() - INTERVAL 3 MONTH), " +
                "'%Y-%m' " +
                ") AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT( " +
                "CURDATE() - INTERVAL 3 MONTH, " +
                "'%Y-%m' " +
                ") " +
                "UNION " +
                "SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT( " +
                "(CURDATE() - INTERVAL 2 MONTH), " +
                "'%Y-%m' " +
                ") AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT( " +
                "CURDATE() - INTERVAL 2 MONTH, " +
                "'%Y-%m' " +
                ") " +
                "UNION " +
                "SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT( " +
                "(CURDATE() - INTERVAL 1 MONTH), " +
                "'%Y-%m' " +
                ") AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT( " +
                "CURDATE() - INTERVAL 1 MONTH, " +
                "'%Y-%m' " +
                ") " +
                "UNION " +
                "SELECT " +
                "SUM(paymentAmount) AS paymentAmount, " +
                "SUM(surplusAmount) AS surplusAmount, " +
                "DATE_FORMAT((CURDATE()), '%Y-%m') AS date " +
                "FROM " +
                "project_detail " +
                "WHERE " +
                "DATE_FORMAT(startTime, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')");

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
