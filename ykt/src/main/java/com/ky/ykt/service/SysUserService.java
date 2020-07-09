package com.ky.ykt.service;

import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mapper.RoleMenuMapper;
import com.ky.ykt.mapper.SysUserMapper;
import com.ky.ykt.mybatis.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    RoleMenuMapper roleMenuMapper;

    MenuMapper menuMapper;

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        List<SysUserEntity> sysUserEntities = sysUserMapper._queryAll(params);
        return new RestResult(sysUserEntities.size(), sysUserEntities);
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<SysUserEntity> list = sysUserMapper._queryPage(params);
        long count = sysUserMapper._queryCount(params);
        return new RestResult(count, list);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(SysUserEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(SysUserEntity SysUserEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._updateEntity(SysUserEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper._deleteForce(id));
    }

    public Object queryById(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserMapper.queryById(params.get("id").toString()));
    }

    public Object doQueryToDo(SysUserEntity user) {
        List<String> strings = roleMenuMapper.queryMenuIdByRoleId(user.getRoleId());
        Map params = new HashMap();
        params.put("menuIdList", strings);
        List<MenuEntity> menuEntities = menuMapper._queryAll(params);
        for (MenuEntity menuEntity : menuEntities) {
            if (menuEntity.getMenuUrl().contains("person.html")) {

            }
            if (menuEntity.getMenuUrl().contains("compare.html")) {

            }
            if (menuEntity.getMenuUrl().contains("projectpull.html")) {

            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, "");
    }
}
