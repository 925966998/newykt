package com.ky.ykt.mapper;

import com.ky.ykt.entity.PersonReplacementEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-09 18:07
 * @version: v1.0
 */
@Mapper
public interface PersonReplacementMapper extends BaseMapper {

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonReplacementSql.class, method = "_queryPage")
    List<PersonReplacementEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PersonReplacementSql.class, method = "_queryAll")
    List<PersonReplacementEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PersonReplacementSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PersonReplacementSql.class, method = "_get")
    PersonReplacementEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonReplacementSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PersonReplacementSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = PersonReplacementSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonReplacementSql.class, method = "_addEntity")
    int _addEntity(PersonReplacementEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = PersonReplacementSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PersonReplacementSql.class, method = "_updateEntity")
    int _updateEntity(PersonReplacementEntity bean);

    @Select("select pr.*,p.name as name,p.phone as phone,p.idCardNo as idCardNo,p.bankCardNo as bankCardNo from person_replacement pr left join person p on pr.personId = p.id where pr.id = #{id}")
    PersonReplacementEntity queryReplacementById(Map params);

    @Select("select * from person_replacement where personId = #{id} and status = 1")
    List<PersonReplacementEntity> queryReplacementBypersonId(Map params);

    @Select("select * from person_replacement where personId = #{personId} and status = 4 and projectId = #{projectId}")
    PersonReplacementEntity queryPersonId(Map params);
}
