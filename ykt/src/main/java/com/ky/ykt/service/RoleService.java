package com.ky.ykt.service;

import com.ky.ykt.entity.RoleEntity;
import com.ky.ykt.mapper.RoleMapper;
import com.ky.ykt.mapper.RoleMenuMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RoleService
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/19
 **/
@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<RoleEntity> list = roleMapper._queryPage(params);
        long count = roleMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public RestResult _get(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._get(id));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._add(params));
    }

    public Object add(RoleEntity RoleEntity) {
        roleMapper._addEntity(RoleEntity);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, RoleEntity.getId());
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._update(params));
    }

    public Object update(RoleEntity RoleEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._updateEntity(RoleEntity));
    }

    /**
     * 逻辑删除
     */
    @Transactional
    public Object delete(String id) {
        roleMenuMapper.deleteByRoleId(id);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMapper._deleteForce(id));
    }

    public Object queryAll(Map params) {
        List<RoleEntity> roleEntities = roleMapper._queryAll(params);
        return roleEntities;
    }
}

