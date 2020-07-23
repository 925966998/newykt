package com.ky.ykt.mapper;

import com.ky.ykt.entity.ProjectAreaEntity;
import com.ky.ykt.entity.ProjectAreaEntity;
import com.ky.ykt.mybatis.BaseMapper;
import com.ky.ykt.mybatis.RestResult;
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
public interface ProjectAreaMapper extends BaseMapper {
    
    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectAreaSql.class, method = "_queryPage")
    List<ProjectAreaEntity> _queryPage(Map pagerParam);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = ProjectAreaSql.class, method = "_queryAll")
    List<ProjectAreaEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectAreaSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = ProjectAreaSql.class, method = "_get")
    ProjectAreaEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectAreaSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectAreaSql.class, method = "_deleteForce")
    int _deleteForce(String id);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = ProjectAreaSql.class, method = "_add")
    int _add(Map params);

    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectAreaSql.class, method = "_addEntity")
    int _addEntity(ProjectAreaEntity bean);

    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = ProjectAreaSql.class, method = "_update")
    int _update(Map params);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectAreaSql.class, method = "_updateEntity")
    int _updateEntity(ProjectAreaEntity bean);
    @Select("select * from project_area where projectId = #{id}")
    List<ProjectAreaEntity> selectProjectId(String id);
    @Select("select * from project_area where projectId = #{projectId} and userId = #{userId} and areaId = #{areaId} ")
    ProjectAreaEntity _queryProjectAreas(Map<Object, Object> params);
}
