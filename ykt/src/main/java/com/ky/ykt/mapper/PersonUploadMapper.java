package com.ky.ykt.mapper;

import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.excle.ExcelHead;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-29 11:15
 * @version: v1.0
 */
@Mapper
public interface PersonUploadMapper extends BaseMapper {

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonUploadSql.class, method = "_queryPage")
    List<PersonUploadEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PersonUploadSql.class, method = "_queryAll")
    List<PersonUploadEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonUploadSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PersonUploadSql.class, method = "_get")
    PersonUploadEntity _get(String id);

    @Select("select p.*,j.projectName as projectName,d.departmentName as departmentName from person_upload p left join project j on p.projectCode=j.projectCode left join department d on p.departmentId=d.id where p.id=#{id}")
    PersonUploadEntity queryById(@Param("id") String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonUploadSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonUploadSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = PersonUploadSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonUploadSql.class, method = "_addEntity")
    int _addEntity(PersonUploadEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = PersonUploadSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonUploadSql.class, method = "_updateEntity")
    int _updateEntity(PersonUploadEntity bean);


    @Select("SELECT COLUMN_NAME entityName,column_comment excelName FROM INFORMATION_SCHEMA.Columns WHERE table_name='person_upload' AND table_schema='ky-ykt'")
    List<ExcelHead> _queryColumnAndComment();

    @Select("select * from person_upload where idCardNo = #{idCardNo}")
    List<PersonUploadEntity> queryByIdCardNo(@Param("idCardNo") String idCardNo);

    @Update("update person_upload set status = 1,submitTime=now() where id = #{id} ")
    int doSubmitAudit(String id);

    @Update("update person_upload set status = #{status},auditReason=#{auditReason},auditTime=now() where id = #{id}")
    int audit(Map params);

    @Select("SELECT SUM(grantAmount) FROM person_upload WHERE status = 0 AND projectId = #{projectId}")
    BigDecimal queryPaymentAmount(@Param("projectId") String projectId);

    @Select("SELECT * FROM person_upload WHERE personId = #{id}")
    PersonUploadEntity _queryPersonId(@Param("id") String id);

    @Select("SELECT departmentId FROM person_upload WHERE personId = #{id} GROUP BY departmentId")
    PersonUploadEntity _queryPersonIdoperDepartmentChildren(@Param("id") String id);

    @Select("SELECT pu.*,d.departmentName AS departmentName,pd.projectName AS projectName ,a1.name as countyName,a2.name as townName ,a3.name as villageName FROM person_upload pu LEFT JOIN  project_detail pd ON pu.projectId = pd.id LEFT JOIN  department d ON d.id = pd.paymentDepartment  left join areas a1 on a1.id=pu.county left join areas a2 on a2.id=pu.town  left join areas a3 on a3.id=pu.village WHERE pu.id = #{id} AND pu.logicalDel = 0 ")
    PersonUploadEntity queryByAll(Map params);

    @Select("select * from person_upload where id = #{personId}")
    PersonUploadEntity querypersonId(@Param("personId") String personId);
}
