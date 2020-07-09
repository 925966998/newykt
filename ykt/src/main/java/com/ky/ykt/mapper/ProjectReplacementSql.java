package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @ClassName ProjectDetailSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
public class ProjectReplacementSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "project_replacement";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"projectDetailId","projectId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select pr.*,pd.* from project_replacement pr left join project_detail pd on pr.projectDetailId = pd.id where 1=1 ");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectDetailId"))) {
            builder.append(" and pd.projectDetailId = #{projectDetailId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pd.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("pd.operDepartment"));
        }
        builder.append(" order by pd.startTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select pr.*,pd.* from project_replacement pr left join project_detail pd on pr.projectDetailId = pd.id where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectDetailId"))) {
            builder.append(" and pd.projectDetailId = #{projectDetailId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pd.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("pd.operDepartment"));
        }
        builder.append(" order by pd.startTime desc");
        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }

    private String dealStartEndDate(String date, String type) {
        if (type.contains("start")) {
            return date + " 00:00:00";
        }
        if (type.contains("end")) {
            return date + " 23:59:59";
        }
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
