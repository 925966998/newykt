package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:35
 * @version: v1.0
 */
public class PermissionSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "sys_permission";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id","parentId","perName","perUrl","perPerms","perIcon","perType","perSort"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "parentId"))) {
            builder.append(" and parentId = #{parentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perName"))) {
            builder.append(" and perName like concat('%',#{perName},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perUrl"))) {
            builder.append(" and perUrl like concat('%',#{perUrl},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perPerms"))) {
            builder.append(" and perPerms like concat('%',#{perPerms},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perIcon"))) {
            builder.append(" and perIcon like concat('%',#{perIcon},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perType"))) {
            builder.append(" and perType = #{perType}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perSort"))) {
            builder.append(" and perSort = #{perSort}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "parentId"))) {
            builder.append(" and parentId = #{parentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perName"))) {
            builder.append(" and perName like concat('%',#{perName},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perUrl"))) {
            builder.append(" and perUrl like concat('%',#{perUrl},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perPerms"))) {
            builder.append(" and perPerms like concat('%',#{perPerms},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perIcon"))) {
            builder.append(" and perIcon like concat('%',#{perIcon},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perType"))) {
            builder.append(" and perType = #{perType}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "perSort"))) {
            builder.append(" and perSort = #{perSort}");
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