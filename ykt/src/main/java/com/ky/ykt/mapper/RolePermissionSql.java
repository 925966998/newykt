package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:36
 * @version: v1.0
 */
public class RolePermissionSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "sys_role_permission";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id","roleId","permissionId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and roleId = #{roleId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleId"))) {
            builder.append(" and permissionId = #{permissionId}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }
}