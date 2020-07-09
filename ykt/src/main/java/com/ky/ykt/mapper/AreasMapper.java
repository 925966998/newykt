package com.ky.ykt.mapper;

import com.ky.ykt.entity.AreasEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-27 16:42
 * @version: v1.0
 */
@Mapper
public interface AreasMapper extends BaseMapper {

    @Select("select * from areas where town = #{town} limit 1")
    AreasEntity queryByTown(@Param("towm") String town);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = AreasSql.class, method = "_queryPage")
    List<AreasEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = AreasSql.class, method = "_queryAll")
    List<AreasEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = AreasSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = AreasSql.class, method = "_get")
    AreasEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = AreasSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = AreasSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = AreasSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = AreasSql.class, method = "_addEntity")
    int _addEntity(AreasEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = AreasSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = AreasSql.class, method = "_updateEntity")
    int _updateEntity(AreasEntity bean);

    @Select("select * from areas where name = #{name} and level = #{level}")
    AreasEntity queryByIdByName(@Param("name") String name, @Param("level") int level);

    @Select("select * from areas where parentId=#{parentId}")
    List<AreasEntity> queryByPid(@Param("parentId") String parentId);

    @Select("select * from areas where name = #{name} and parentId=#{parentId}")
    List<AreasEntity> queryByAreasPid(@Param("name") String name, @Param("parentId") String parentId);
}
