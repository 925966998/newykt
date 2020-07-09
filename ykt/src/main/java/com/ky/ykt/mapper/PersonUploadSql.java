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
 * @createTime: 2020-02-29 11:15
 * @version: v1.0
 */
public class PersonUploadSql extends BaseProvider {
    @Override
    protected String getTableName() {
        return "person_upload";
    }

    @Override
    protected String[] getColumns() {
        return new String[]{"name", "phone", "idCardNo", "projectId", "departmentId", "county", "town", "village", "address", "bankCardNo", "grantAmount", "status", "personId", "openingBank","projectType"};
    }

    @Override
    protected String _query(Map map) {
        StringBuilder builder = new StringBuilder("SELECT\n" +
                "\tpu.*, d.departmentName AS departmentName,\n" +
                "\tpt.name AS projectTypeName,\n" +
                "a1.name as countyName,a2.name as townName ,a3.name as villageName " +
                "FROM\n" +
                "\tperson_upload pu\n" +
                "LEFT JOIN department d ON d.id = pu.departmentId\n" +
                "left join areas a1 on a1.id=pu.county left join areas a2 on a2.id=pu.town  left join areas a3 on a3.id=pu.village " +
                "LEFT JOIN project_type pt ON pt.id = pu.projectType\n" +
                "WHERE\n" +
                "\t1 = 1");
        if (StringUtils.isNotBlank(MapUtils.getString(map, "phone"))) {
            builder.append(" and pu.phone = #{phone}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "idCardNo"))) {
            builder.append(" and pu.idCardNo = #{idCardNo}");
        }    if (StringUtils.isNotBlank(MapUtils.getString(map, "projectType"))) {
            builder.append(" and pu.projectType = #{projectType}");
        }
//        if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentId"))) {
//            builder.append(" and pu.departmentId = #{departmentId}");
//        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "grantAmount"))) {
            builder.append(" and pu.grantAmount = #{grantAmount}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "projectId"))) {
            builder.append(" and pu.projectId = #{projectId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "name"))) {
            builder.append(" and pu.name like  concat('%',#{name},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "bankCardNo"))) {
            builder.append(" and pu.bankCardNo = #{bankCardNo}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "openingBank"))) {
            builder.append(" and pu.openingBank = #{openingBank}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "personId"))) {
            builder.append(" and pu.personId = #{personId}");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "county"))) {
            builder.append(" and pu.county like  concat('%',#{county},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "address"))) {
            builder.append(" and pu.address like  concat('%',#{address},'%')");
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "level"))) {
            if (map.get("level").toString().equals("2")) {
                builder.append(" and pu.county = #{areaId}");
            } else if (map.get("level").toString().equals("3")) {
                builder.append(" and pu.town = #{areaId}");
            } else if (map.get("level").toString().equals("4")) {
                builder.append(" and pu.village = #{areaId}");
            }
        }
        if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("1")) {
//            builder.append(GetDepartmentSql.getUserBuilder("pu.departmentId"));
        } else if (StringUtils.isNotBlank(MapUtils.getString(map, "flag")) && map.get("flag").equals("2")) {
            if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdListFlag")) && map.get("departmentIdListFlag").equals("departmentIdListFlag")) {
                if (StringUtils.isNotBlank(MapUtils.getString(map, "departmentIdList"))) {
                    builder.append(" and pu.departmentId in (");
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
            }
        }
        builder.append(" order by pu.updateTime desc");
        return builder.toString();
    }
}