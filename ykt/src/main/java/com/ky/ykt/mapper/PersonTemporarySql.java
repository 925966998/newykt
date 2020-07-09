package com.ky.ykt.mapper;

import com.ky.ykt.mybatis.BaseProvider;
import com.ky.ykt.utils.GetDepartmentSql;
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
public class PersonTemporarySql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "person_temporary";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"name","phone","idCardNo","projectId","departmentId","county","address","bankCardNo","grantAmount","status","personId","userId"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("SELECT\n" +
                "\tpt.*, d.departmentName AS departmentName,\n" +
                "\tpd.projectName AS projectName,\n" +
                "\tac.cname AS cname\n" +
                "FROM\n" +
                "\tperson_temporary pt\n" +
                "LEFT JOIN project_detail pd ON pt.projectId = pd.id\n" +
                "LEFT JOIN department d ON d.id = pd.paymentDepartment\n" +
                "LEFT JOIN areas_county ac ON pt.county = ac.id\n" +
                "LEFT JOIN project p ON p.id = pd.projectId\n" +
                "WHERE\n" +
                "\t1 = 1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and pu.name like  concat('%',#{name},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "phone"))) {
            builder.append(" and pu.phone = #{phone}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "idCardNo"))) {
            builder.append(" and pu.idCardNo = #{idCardNo}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentId"))) {
            builder.append(" and pu.departmentId = #{departmentId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "county"))) {
            builder.append(" and pu.county = #{county}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "address"))) {
            builder.append(" and pu.address like  concat('%',#{address},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "bankCardNo"))) {
            builder.append(" and pu.bankCardNo = #{bankCardNo}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "grantAmount"))) {
            builder.append(" and pu.grantAmount = #{grantAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pu.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "personId"))) {
            builder.append(" and pu.personId = #{personId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "userId"))) {
            builder.append(" and pu.userId = #{userId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
            builder.append(GetDepartmentSql.getUserBuilder("p.operDepartment"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            builder.append(GetDepartmentSql.getUserBuilder("pd.operDepartment"));
        }
        builder.append(" order by pt.updateTime desc");
        return builder.toString();
    }
}