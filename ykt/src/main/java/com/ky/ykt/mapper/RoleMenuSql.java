package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class RoleMenuSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "role_menu";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{"menuId", "roleId"
        };
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "menuId"))) {
            builder.append(" and menuId = #{menuId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "roleId"))) {
            builder.append(" and roleId = #{roleId}");
        }

        if (StringUtils.isNotBlank(MapUtils.getString(map, "menuIdList"))) {
            builder.append(" and menuId in (");
            if (map.get("menuIdList") instanceof List) {
                List<String> menuIdList = (List) map.get("menuIdList");
                for (String id : menuIdList) {
                    if (menuIdList.indexOf(id) > 0)
                        builder.append(",");
                    builder.append("'").append(id).append("'");
                }
            } else {
                builder.append(map.get("menuIdList"));
            }
            builder.append(")");
        }
        builder.append(" order by updateTime desc");
        return builder.toString();
    }

}
