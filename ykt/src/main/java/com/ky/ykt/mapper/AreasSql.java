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
public class AreasSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "areas";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"code", "name", "level","parentId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name like concat('%',#{name},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "code"))) {
            builder.append(" and code = #{code}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "level"))) {
            builder.append(" and level =#{level}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "parentId"))) {
            builder.append(" and parentId =#{parentId}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "city"))) {
            builder.append(" and city like concat('%',#{city},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "county"))) {
            builder.append(" and county like concat('%',#{county},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "town"))) {
            builder.append(" and town like concat('%',#{town},'%')");
        }if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and name like concat('%',#{name},'%')");
        }if (StringUtils.isNotBlank(MapUtils.getString(map, "parentId"))) {
            builder.append(" and parentId =#{parentId}");
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