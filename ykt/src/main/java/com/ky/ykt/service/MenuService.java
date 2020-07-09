package com.ky.ykt.service;

import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuService {

    @Autowired
    MenuMapper menuMapper;

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List<MenuEntity> queryAll(Map params) {
        List<MenuEntity> quotaEntities = menuMapper._queryAll(params);
        return quotaEntities;
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<MenuEntity> list = menuMapper._queryPage(params);
        long count = menuMapper._queryCount(params);
        return new RestResult(count, list);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(MenuEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(MenuEntity PersonEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._updateEntity(PersonEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, menuMapper._deleteForce(id));
    }

    public MenuEntity queryByMenuName(String menuName) {
        return menuMapper.queryByMenuName(menuName);
    }

    public List<MenuEntity> queryByPid(String pid) {
        return menuMapper.queryByPid(pid);
    }

    public Integer queryCurMostSort(Map params) {
        if (StringUtils.isNotBlank(MapUtils.getString(params, "isFirstMenu"))) {
            return menuMapper.queryCurMostSortByIsFirstMenu(params.get("isFirstMenu").toString());
        } else {
            return menuMapper.queryCurMostSortByParentId(params.get("parentId").toString());
        }
    }
}