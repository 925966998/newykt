package com.ky.ykt.mapper;

import com.ky.ykt.entity.PersonEntity;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.entity.StatisticEntity;
import com.ky.ykt.excle.ExcelHead;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface PersonMapper extends BaseMapper {

    @Select("select count(*) as num from person where TRIM(projectId) = #{id}")
    long queryCountByProjectCode(@Param("id") String id);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonSql.class, method = "_queryPage")
    List<PersonEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PersonSql.class, method = "_queryAll")
    List<PersonEntity> _queryAll(Map pagerParam);

    @SelectProvider(type = PersonSql.class, method = "statistics")
    List<StatisticEntity> statistics(Map pagerParam);

    @SelectProvider(type = PersonSql.class, method = "statisticsCount")
    long statisticsCount(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PersonSql.class, method = "_get")
    PersonEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = PersonSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonSql.class, method = "_addEntity")
    int _addEntity(PersonEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = PersonSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonSql.class, method = "_updateEntity")
    int _updateEntity(PersonEntity bean);


    @Select("SELECT COLUMN_NAME entityName,column_comment excelName FROM INFORMATION_SCHEMA.Columns WHERE table_name='person' AND table_schema='ky-ykt'")
    List<ExcelHead> _queryColumnAndComment();

    @Select("select * from person where idCardNo = #{idCardNo}")
    List<PersonEntity> queryByIdCardNo(@Param("idCardNo") String idCardNo);

    @Update("update person set projectId=#{projectId},status=4 where id = #{id} ")
    int doSubmitAudit(@Param("id") String id, @Param("projectId") String projectId);

    @Update("update person set status = #{status},auditReason=#{auditReason},auditTime=now() where id = #{id}")
    int audit(Map params);

    @Update("update person set status = 3  where id = #{id}")
    void submitToBuss(@Param("id") String id);

    @Select("SELECT p.*,d.departmentName AS departmentName,pt.name as projectName ,a1.name as countyName,a2.name as townName ,a3.name as villageName  FROM person p LEFT JOIN department d ON p.departmentId=d.id LEFT JOIN project_detail pd ON p.projectId=pd.id   left join areas a1 on a1.id=p.county left join areas a2 on a2.id=p.town  left join areas a3 on a3.id=p.village  left join project_type pt on pd.projectName=pt.id WHERE p.id = #{id}")
    PersonEntity queryByAll(Map params);

    @Select("SELECT SUM(grantAmount) FROM person WHERE  projectId = #{projectId} and departmentId = #{departmentId} and logicalDel = 0")
    BigDecimal queryMoney(Map map);

    @Update("update person set status ='4'  where projectId =#{id}")
    int updateByProjectId(@Param("id") String id);

    @SelectProvider(type = PersonSql.class, method = "_queryByPage")
    List<PersonEntity> _queryByPage(Map params);

    @Select("select * from person where id = #{personId} ")
    PersonEntity querypersonId(@Param("personId") String personId);

    @Select("select * from person where name = #{name} and  idCardNo = #{idCardNo} and bankCardNo = #{bankCardNo} and status = 4 order by createTime")
    List<PersonEntity> queryWechatPerson(Map params);

    @Select("select *,a1.`name` AS countyName,a2.`name` as townName,a3.`name` AS villageName from person_Upload pu left join areas a1 on a1.id=pu.county left join areas a2 on a2.id=pu.town  left join areas a3 on a3.id=pu.village where pu.name = #{name} and  pu.idCardNo = #{idCardNo} and pu.bankCardNo = #{bankCardNo}")
    PersonUploadEntity queryPerson(Map params);
}
