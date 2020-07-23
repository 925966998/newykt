package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class UserProjectTypeSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "user_projecttype";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{"userId", "projectTypeId"
        };
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectTypeId"))) {
            builder.append(" and projectTypeId = #{projectTypeId}");
        }

        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectTypeIdList"))) {
            builder.append(" and projectTypeId in (");
            if (map.get("projectTypeIdList") instanceof List) {
                List<String> menuIdList = (List) map.get("projectTypeIdList");
                for (String id : menuIdList) {
                    if (menuIdList.indexOf(id) > 0)
                        builder.append(",");
                    builder.append("'").append(id).append("'");
                }
            } else {
                builder.append(map.get("projectTypeIdList"));
            }
            builder.append(")");
        }
        builder.append(" order by updateTime desc");
        return builder.toString();
    }

}
