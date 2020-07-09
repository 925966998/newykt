package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-09 18:07
 * @version: v1.0
 */
public class PersonReplacementSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "person_replacement";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"personId", "replacementAmount", "departmentId", "userId", "projectId", "status"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("select pr.*,p.name as name,p.phone as phone,p.idCardNo as idCardNo,p.bankCardNo as bankCardNo from person_replacement pr left join person p on pr.personId = p.id ");
        builder.append(" LEFT JOIN department d ON d.id = pr.departmentId");
        builder.append(" WHERE 1 = 1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "personId"))) {
            builder.append(" and pr.personId = #{personId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "replacementAmount"))) {
            builder.append(" and pr.replacementAmount = #{replacementAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentId"))) {
            builder.append(" and pr.departmentId = #{departmentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and pr.userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "status"))) {
            builder.append(" and pr.status = #{status}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("d.departmentId"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdListFlag")) && map.get("departmentIdListFlag").equals("departmentIdListFlag")) {
                if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdList"))) {
                    builder.append(" and p.departmentId in (");
                    if (map.get("departmentIdList") instanceof List) {
                        List<String> departmentIdList = (List) map.get("departmentIdList");
                        for (String id : departmentIdList) {
                            if (departmentIdList.indexOf(id) > 0) {
                                builder.append(",");
                            }
                            builder.append("'").append(id).append("'");
                        }
                    } else {
                        builder.append(map.get("departmentIdList"));
                    }
                    builder.append(")");
                }
            } else {
                builder.append(GetDepartmentSql.getUserBuilder("p.departmentId"));
            }
        }
        builder.append(" order by pr.updateTime desc");
        return builder.toString();
    }

    @Override
    public String _queryPage(Map map) {
        StringBuilder builder = new StringBuilder("select pr.*,p.name as name,p.phone as phone,p.idCardNo as idCardNo,p.bankCardNo as bankCardNo from person_replacement pr left join person p on pr.personId = p.id WHERE 1=1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "personId"))) {
            builder.append(" and pr.personId = #{personId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "replacementAmount"))) {
            builder.append(" and pr.replacementAmount = #{replacementAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentId"))) {
            builder.append(" and pr.departmentId = #{departmentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and pr.userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "status"))) {
            builder.append(" and pr.status = #{status}");
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