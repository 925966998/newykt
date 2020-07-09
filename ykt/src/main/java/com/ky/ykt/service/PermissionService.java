package com.ky.ykt.service;

import com.ky.ykt.entity.PermissionEntity;
import com.ky.ykt.mapper.PermissionMapper;
import com.ky.ykt.mapper.UserRoleMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 18:13
 * @version: v1.0
 */
@Service
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);
    @Autowired
    PermissionMapper permissionMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    public List<PermissionEntity> queryByParentId(String parentId) {
        return permissionMapper.queryByParentId(parentId);
    }

    public Object update(PermissionEntity permissionEntity) {
        return permissionMapper._updateEntity(permissionEntity);
    }

    /**
     * 新增菜单
     * @param permissionEntity
     * @return
     */
    public Object add(PermissionEntity permissionEntity) {
       return permissionMapper._addEntity(permissionEntity);
    }

    public void delete(String id) {
        permissionMapper._delete(id);
        permissionMapper.deleteParentId(id);
        permissionMapper.deleteRolePermission(id);
    }

    /**
     * 通过用户查询权限
     * @param userId
     * @return
     */
    public Object queryByUser(String userId) {

        return permissionMapper.queryByUser(userId);
        /*
        List<UserRoleEntity> userRoleList = userRoleMapper.queryByUserId(userId);
        for (UserRoleEntity roleId: userRoleList) {
            List<RolePermissionEntity> rolePermissionList = rolePermissionMapper.queryByRoleId(roleId);
            for (RolePermissionEntity perId: rolePermissionList) {
                List<PermissionEntity> permissionList = permissionMapper.queryById(perId);
                return permissionList;
            }
        }
        */

    }

    /**
     * 分页查询
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<PermissionEntity> list = permissionMapper._queryPage(params);
        long count = permissionMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }
}