package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class PersonPullSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "person_pull";
    }

    // 涉及到插入和更新的字段，不在该定义中的字段不会被操作
    @Override
    protected String[] getColumns() {
        return new String[]{"personId", "projectId","projectDetailId","state", "failReason", "departmentId", "userId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("SELECT p.*,pd.projectName as projectName,d.departmentName AS departmentName,pe.idCardNo AS idCardNo,pe.bankCardNo AS bankCardNo,pe.grantAmount AS grantAmount,pe.name AS name,pe.openingBank AS openingBank FROM person_pull p");
        builder.append(" left join project pd on pd.id=p.projectId");
        builder.append(" left join person pe on pe.id=p.personId");
        builder.append(" LEFT JOIN department d ON d.id = p.departmentId  ");
        builder.append(" WHERE 1 = 1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "personId"))) {
            builder.append(" and p.personId = #{personId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and p.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "state"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "failReason"))) {
            builder.append(" and p.failReason = #{failReason}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectDetailId"))) {
            builder.append(" and p.projectDetailId = #{projectDetailId}");
        }
        /*if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentId"))) {
            builder.append(" and p.departmentId = #{departmentId}");
        }*/
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and p.userId = #{userId}");
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
                            if (departmentIdList.indexOf(id) > 0)
                                builder.append(",");
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
        builder.append(" order by p.updateTime desc");
        return builder.toString();
    }

    public String _queryByPage(Map map) {
        StringBuilder builder = new StringBuilder("SELECT p.*,d.departmentName AS departmentName,pd.projectName as projectName FROM person_pull p ");
        builder.append(" left join project_detail pd on pd.projectId =p.projectId");
        builder.append(" left join person pe on pe.id=p.personId");
        builder.append(" LEFT JOIN department d ON d.id = p.departmentId  ");
        builder.append(" WHERE 1 = 1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and p.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and p.userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "status"))) {
            builder.append(" and p.state = #{state}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("d.departmentId"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.departmentId"));
        }
        builder.append(" order by p.updateTime desc");
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
