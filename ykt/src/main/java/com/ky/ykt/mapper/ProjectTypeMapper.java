package com.ky.ykt.mapper;

import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.entity.ProjectTypeEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DepartmentMapper
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/18
 **/
@Mapper
public interface ProjectTypeMapper extends BaseMapper {


    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectTypeSql.class, method = "_queryPage")
    List<ProjectTypeEntity> _queryPage(Map pagerParam);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = ProjectTypeSql.class, method = "_add")
    int _add(Map params);


    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = ProjectTypeSql.class, method = "_update")
    int _update(Map params);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = ProjectTypeSql.class, method = "_queryAll")
    List<ProjectTypeEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectTypeSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = ProjectTypeSql.class, method = "_get")
    ProjectTypeEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectTypeSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectTypeSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectTypeSql.class, method = "_addEntity")
    int _addEntity(ProjectTypeEntity bean);


    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectTypeSql.class, method = "_updateEntity")
    int _updateEntity(ProjectTypeEntity bean);
    @Select("select * from project_type where id = #{id}")
    List<ProjectTypeEntity> queryByProjectTypeId(String id);
}
