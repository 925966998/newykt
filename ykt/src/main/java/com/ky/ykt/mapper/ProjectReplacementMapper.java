package com.ky.ykt.mapper;

import com.ky.ykt.entity.ProjectReplacementEntity;
import com.ky.ykt.entity.ProjectReplacementEntity;
import com.ky.ykt.entity.StatisticEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectDetailMapper
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@Mapper
public interface ProjectReplacementMapper extends BaseMapper {


    @Select("select pd.projectName,pd.paymentAmount,pd.totalAmount,d.departmentName,su.userName from project_detail pd LEFT JOIN department d on d.id=pd.operDepartment LEFT JOIN sys_user su on pd.operUser=su.id  where pd.id=#{id}")
    Map<String, Object> queryById(@Param("id")String id);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectReplacementSql.class, method = "_queryPage")
    List<ProjectReplacementEntity> _queryPage(Map pagerParam);

    @SelectProvider(type = ProjectReplacementSql.class, method = "statistic")
    List<StatisticEntity> statistic(Map pagerParam);
    /*
    @SelectProvider(type = ProjectReplacementSql.class, method = "statisticPage")
    List<StatisticEntity> statisticPage(Map pagerParam);

    @SelectProvider(type = ProjectReplacementSql.class, method = "statisticCount")
    BigDecimal statisticCount(Map pagerParam);

    @SelectProvider(type = ProjectReplacementSql.class, method = "statisticPageCount")
    long statisticPageCount(Map pagerParam);
    */
    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = ProjectReplacementSql.class, method = "_add")
    int _add(Map params);


    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = ProjectReplacementSql.class, method = "_update")
    int _update(Map params);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = ProjectReplacementSql.class, method = "_queryAll")
    List<ProjectReplacementEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectReplacementSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = ProjectReplacementSql.class, method = "_get")
    ProjectReplacementEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectReplacementSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectReplacementSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectReplacementSql.class, method = "_addEntity")
    int _addEntity(ProjectReplacementEntity bean);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectReplacementSql.class, method = "_updateEntity")
    int _updateEntity(ProjectReplacementEntity bean);

    @Select("select id from project_detail where projectId = #{projectId}")
    List<ProjectReplacementEntity> _queryProjectId(Map params);
}
