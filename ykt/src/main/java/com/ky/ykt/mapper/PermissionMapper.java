package com.ky.ykt.mapper;

import com.ky.ykt.entity.PermissionEntity;
import com.ky.ykt.entity.RolePermissionEntity;
import com.ky.ykt.mybatis.BaseMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:31
 * @version: v1.0
 */
@Mapper
public interface PermissionMapper extends BaseMapper {

    /**
     * 通过parentID查找
     *
     * @param parentId
     * @return
     */
    @Select("select * from sys_permission  where parentId = #{parentId} and logicalDel=0 order by perSort")
    List<PermissionEntity> queryByParentId(@Param("parentId") String parentId);

    /**
     * 通过ID查找
     *
     * @param Id
     * @return
     */
    @Select("select * from sys_permission where id = #{id} and logicalDel=0 order by perSort")
    List<PermissionEntity> queryById(@Param("Id") RolePermissionEntity Id);

    /**
     * 通过UserId查询
     *
     * @param userId
     * @return
     */
    @Select("select distinct p.* from sys_permission p inner join sys_role_permission rp on p.id = rp.permissionId inner join sys_user_role ur on ur.roleId = rp.roleId where ur.userId = #{userId} and ur.logicalDel=0 order by p.perSort")
    Object queryByUser(@Param("userId") String userId);

    /**
     * 根据条件查询分页 必要参数： currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10条 其他参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于
     * 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PermissionSql.class, method = "_queryPage")
    List<PermissionEntity> _queryPage(Map pagerParam);

    /**
     * 新增 参数： map里的key为属性名（字段首字母小写） value为要插入的key值
     */
    @SuppressWarnings("rawtypes")
    @InsertProvider(type = PermissionSql.class, method = "_add")
    int _add(Map params);


    /**
     * 更新 参数： id ： 要更新的记录的id 其他map里的参数，key为属性名（字段首字母小写） value为要更新成的值
     */
    @InsertProvider(type = PermissionSql.class, method = "_update")
    int _update(Map params);

    /**
     * 根据条件查询全部 参数： map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SuppressWarnings("rawtypes")
    @SelectProvider(type = PermissionSql.class, method = "_queryAll")
    List<PermissionEntity> _queryAll(Map pagerParam);

    /**
     * 根据条件查询总条数 map里的key为属性名（字段首字母小写） value为查询的条件，默认为等于 要改动sql请修改 *Mapper 类里的 _query() 方法
     */
    @SelectProvider(type = PermissionSql.class, method = "_queryCount")
    long _queryCount(Map params);

    /**
     * 按id查询 参数： id ： 要查询的记录的id
     */
    @SelectProvider(type = PermissionSql.class, method = "_get")
    PermissionEntity _get(String id);

    /**
     * 删除（逻辑） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PermissionSql.class, method = "_delete")
    int _delete(String id);

    /**
     * 删除（物理） 参数： id ： 要删除的记录的id
     */
    @DeleteProvider(type = PermissionSql.class, method = "_deleteForce")
    int _deleteForce(String id);


    /**
     * 按实体类新增 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = PermissionSql.class, method = "_addEntity")
    int _addEntity(PermissionEntity bean);


    /**
     * 按实体类更新 参数： 实体类对象，必须有id属性
     */
    @InsertProvider(type = DepartmentSql.class, method = "_updateEntity")
    int _updateEntity(PermissionEntity bean);

    /**
     * 删除父级目录
     *
     * @param id
     */
    @Update("update sys_permission set logicalDel = 1 where parentId = #{id}")
    void deleteParentId(String id);

    /**
     * @param id
     */
    @Update("update sys_role_permission set logicalDel = 1 where permissionId = #{id}")
    void deleteRolePermission(String id);
}
