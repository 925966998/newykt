package com.ky.ykt.mapper;

import com.ky.ykt.entity.PersonPullEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface PersonPullMapper extends BaseMapper {

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonPullSql.class, method = "_queryPage")
    List<PersonPullEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PersonPullSql.class, method = "_queryAll")
    List<PersonPullEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonPullSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PersonPullSql.class, method = "_get")
    PersonPullEntity _get(String id);

    @SelectProvider(type = PersonPullSql.class, method = "_queryAll")
    PersonPullEntity _queryPull(Map params);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonPullSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonPullSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = PersonPullSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonPullSql.class, method = "_addEntity")
    int _addEntity(PersonPullEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = PersonPullSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonPullSql.class, method = "_updateEntity")
    int _updateEntity(PersonPullEntity bean);


    @Select("SELECT p.*,pd.projectName as projectName,d.departmentName AS departmentName,pe.idCardNo AS idCardNo,pe.bankCardNo AS bankCardNo,pe.grantAmount AS grantAmount,pe.name AS name,pe.openingBank AS openingBank,pe.id AS personId FROM person_pull p LEFT JOIN department d ON p.departmentId=d.id LEFT JOIN project pd ON p.projectId=pd.id LEFT JOIN person pe ON p.personId=pe.id WHERE p.id = #{id}")
    PersonPullEntity queryByAll(Map params);


    @SelectProvider(type = PersonPullSql.class, method = "_queryByPage")
    List<PersonPullEntity> _queryByPage(Map params);

    @Select("select * from person_pull where personId=#{personId} and projectId =#{projectId}")
    PersonPullEntity queryByPersonId(@Param("personId")String personId,@Param("projectId")String projectId);

    @Update("update person_pull set projectDetailId=#{projectDetailId},state=0 where id = #{id} ")
    int reCheck(@Param("id")String id,@Param("projectDetailId") String projectDetailId);

    @Select("select count(id) from person_pull where projectDetailId=#{projectDetailId} and state=1")
    int queryState(Map map);
}
