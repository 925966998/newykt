package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName UserLogSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
public class UserLogSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "user_log";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id", "content", "description", "ip", "module", "userName"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userName"))) {
            builder.append(" and userName = #{userName}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryCount(Map map) {
        StringBuilder builder = new StringBuilder("select count(1) from " + this.getTableName() + " where 1=1 and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userName"))) {
            builder.append(" and userName = #{userName}");
        }
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isBlank(MapUtils.getString(map, "startTime")))
            startTime = dealStartEndDate(map.get("startTime").toString(), "startTime");
        if (!StringUtils.isBlank(MapUtils.getString(map, "endTime")))
            endTime = dealStartEndDate(map.get("endTime").toString(), "endTime");
        if (!startTime.equals("") || !endTime.equals("")) {
            builder.append(" and  createTime between '" + startTime + "' and '" + endTime + "' ");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1 and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userName"))) {
            builder.append(" and userName = #{userName}");
        }
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isBlank(MapUtils.getString(map, "startTime")))
            startTime = dealStartEndDate(map.get("startTime").toString(), "startTime");
        if (!StringUtils.isBlank(MapUtils.getString(map, "endTime")))
            endTime = dealStartEndDate(map.get("endTime").toString(), "endTime");
        if (!startTime.equals("") || !endTime.equals("")) {
            builder.append(" and  createTime between '" + startTime + "' and '" + endTime + "' ");
        }
        builder.append(" order by createTime desc");
        builder.append(this.pageHelp(MapUtils.getLongValue(map, "page"), MapUtils.getLongValue(map, "rows")));
        return builder.toString();
    }

    public StringBuilder pageHelp(long currentPage, long pageSize) {
        long count = (currentPage - 1) * pageSize;
        if (count != 0) {
            count = count ;
        }
        StringBuilder builder = new StringBuilder(" limit ");
        builder.append(count);
        builder.append(" ,");
        builder.append(pageSize);
        return builder;
    }

    private String dealStartEndDate(String date, String type) {
        if (type.contains("start"))
            return date + " 00:00:00";
        if (type.contains("end"))
            return date + " 23:59:59";
        return date;
    }

}
