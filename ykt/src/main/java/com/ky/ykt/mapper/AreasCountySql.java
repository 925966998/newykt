package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-29 8:22
 * @version: v1.0
 */
public class AreasCountySql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "areas_county";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"id","cname"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select * from " + this.getTableName() + " where 1=1  and logicalDel=0");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "cname"))) {
            builder.append(" and cname like concat('%',#{cname},'%')");
        }
        builder.append(" order by createTime desc");
        return builder.toString();
    }
}