package com.ky.ykt.mybatis;

import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * Created by Linan
 */
public abstract class PageProvider {
    protected final static int DIALECT_MYSQL = 1;
    protected final static int DIALECT_ORACLE = 2;
    protected final static int DIALECT_SQL = 3;

    protected int getDialect() {
        return DIALECT_MYSQL;
    }

    protected abstract String _query(Map map);

    public String _queryAll(Map param) {
        return _query(param);
    }


    public String _queryCount(Map map) {
        StringBuilder builder = new StringBuilder("select count(1) from (");
        builder.append(_query(map));
        builder.append(") t");
        return builder.toString();
    }

    public String _queryPage(Map param) {
        long pageSize = MapUtils.getLongValue(param, "pageSize");
        pageSize = pageSize <= 0 ? 10 : pageSize;
        long currentPage = MapUtils.getLongValue(param, "currentPage");
        currentPage = currentPage <= 0 ? 1 : currentPage;
        StringBuilder builder = new StringBuilder();
        if (getDialect() == DIALECT_MYSQL) {
            builder.append(_query(param));
            builder.append(" limit ").append((currentPage - 1) * pageSize).append(",").append(pageSize);
        } else if (getDialect() == DIALECT_ORACLE) {
            builder.append(" SELECT * FROM (SELECT m.*,rownum AS rn FROM( ").append(_query(param))
                    .append(" ) m ) r where r.rn between ")
                    .append((currentPage - 1L) * pageSize + 1L).append(" and ").append(currentPage * pageSize).append(" ");
        } else if (getDialect() == DIALECT_SQL) {
            builder.append("select * from ( ").
                    append(_query(param)).
                    append(" ) tt order by tt.id OFFSET ").
                    append((currentPage - 1L) * pageSize).append(" ROWS FETCH next ").
                    append(pageSize).append(" rows only");
        }
        return builder.toString();
    }
}
