package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @ClassName RoleSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
public class RoleSql extends BaseProvider {

    @Override
    protected String getTableName() {
        return "role";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"roleName", "roleCode", "note"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1 and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleName"))) {
            builder.append(" and roleName = #{roleName}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1 and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleName"))) {
            builder.append(" and roleName = #{roleName}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }

    @Override
    public String _queryCount(Map map) {
        StringBuilder builder = new StringBuilder("select count(1) from " + this.getTableName() + " where 1=1 and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleName"))) {
            builder.append(" and roleName = #{roleName}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }
}
