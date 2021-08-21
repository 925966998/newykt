package com.ky.ykt.mapper;

import com.ky.ykt.entity.ProjectDetailEntity;
import com.ky.ykt.entity.ProjectEntity;
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
public interface ProjectDetailMapper extends BaseMapper {


    @Select("select pd.projectName,SUM(p.grantAmount) AS paymentAmount,pd.totalAmount,d.departmentName,su.userName,pt.name as projectTypeName from project_detail pd LEFT JOIN department d on d.id=pd.operDepartment LEFT JOIN sys_user su on pd.operUser=su.id LEFT JOIN project_type pt on pt.id = pd.projectName LEFT JOIN person p on p.projectId = pd.id  where pd.id=#{id} and p.`status` = '0'")
    Map<String, Object> queryById(@Param("id")String id);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectDetailSql.class, method = "_queryPage")
    List<ProjectDetailEntity> _queryPage(Map pagerParam);

    @SelectProvider(type = ProjectDetailSql.class, method = "statistic")
    List<StatisticEntity> statistic(Map pagerParam);

    @SelectProvider(type = ProjectDetailSql.class, method = "statisticPage")
    List<StatisticEntity> statisticPage(Map pagerParam);

    @SelectProvider(type = ProjectDetailSql.class, method = "statisticCount")
    BigDecimal statisticCount(Map pagerParam);

    @SelectProvider(type = ProjectDetailSql.class, method = "statisticPageCount")
    long statisticPageCount(Map pagerParam);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = ProjectDetailSql.class, method = "_add")
    int _add(Map params);


    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = ProjectDetailSql.class, method = "_update")
    int _update(Map params);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = ProjectDetailSql.class, method = "_queryAll")
    List<ProjectDetailEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = ProjectDetailSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = ProjectDetailSql.class, method = "_get")
    ProjectDetailEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectDetailSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = ProjectDetailSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectDetailSql.class, method = "_addEntity")
    int _addEntity(ProjectDetailEntity bean);

    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = ProjectDetailSql.class, method = "_updateEntity")
    int _updateEntity(ProjectDetailEntity bean);

    @Select("select id from project_detail where projectId = #{projectId}")
    List<ProjectDetailEntity> _queryProjectId(@Param("projectId")Map projectId);
    @Select("select * from project_detail where id = #{projectId}")
    ProjectDetailEntity queryId(@Param("projectId")String projectId);
}
