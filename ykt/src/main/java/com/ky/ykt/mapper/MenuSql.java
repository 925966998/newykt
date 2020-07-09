package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class MenuSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "menu";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{"menuName", "menuUrl", "menuIcon", "menuClass",
                "isFirstMenu", "parentId","menuSort"
        };
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "menuName"))) {
            builder.append(" and menuName like concat('%',#{menuName},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "parentId"))) {
            builder.append(" and parentId = #{parentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "isFirstMenu"))) {
            builder.append(" and isFirstMenu = #{isFirstMenu}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "menuIdList"))) {
            builder.append(" and id in (");
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
        builder.append(" order by menuSort,parentId asc");
        return builder.toString();
    }
}
