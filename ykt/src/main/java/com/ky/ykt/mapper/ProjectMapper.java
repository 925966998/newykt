package com.ky.ykt.mapper;

import com.ky.ykt.entity.ProjectEntity;
import com.ky.ykt.entity.StatisticEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectMapper
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@Mapper
public interface ProjectMapper extends BaseMapper {
    @Select("select * from project where projectCode=#{projectCode}")
    ProjectEntity queryByCode(@Param("projectCode")String projectCode);

    @SelectProvider(type = ProjectSql.class, method = "queryType")
    List<StatisticEntity> queryType(Map params);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectSql.class, method = "_queryPage")
    List<ProjectEntity> _queryPage(Map pagerParam);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = ProjectSql.class, method = "_add")
    int _add(Map params);


    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = ProjectSql.class, method = "_update")
    int _update(Map params);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = ProjectSql.class, method = "_queryAll")
    List<ProjectEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = ProjectSql.class, method = "_get")
    ProjectEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectSql.class, method = "_addEntity")
    int _addEntity(ProjectEntity bean);


    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectSql.class, method = "_updateEntity")
    int _updateEntity(ProjectEntity bean);

    @Select("select * from project where projectType = #{id} and state = 0 and logicalDel = 0")
    List<ProjectEntity> queryProjectType(String id);

    @SelectProvider(type = ProjectSql.class, method = "queryProject")
    List<ProjectEntity> queryProject(Map params);

    @SelectProvider(type = ProjectSql.class, method = "queryFFproject")
    List<ProjectEntity> queryFFproject(Map params);
    @Select("select sum(paymentAmount) from project where projectSourceId = #{id}")
    BigDecimal querySuccess(String id);


}
