package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:33
 * @version: v1.0
 */
public class UserRoleSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "sys_user_role";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id","userId","roleId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleId"))) {
            builder.append(" and roleId = #{roleId}");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }
}