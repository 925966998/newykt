package com.ky.ykt.service;

import com.ky.ykt.entity.RoleMenuEntity;
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
import java.util.UUID;

/**
 * @ClassName RoleService
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/19
 **/
@Service
public class RoleMenuService {
    private static final Logger logger = LoggerFactory.getLogger(RoleMenuService.class);

    @Autowired
    RoleMenuMapper roleMenuMapper;

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<RoleMenuEntity> list = roleMenuMapper._queryPage(params);
        long count = roleMenuMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public RestResult _get(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._get(id));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._add(params));
    }

    public Object add(RoleMenuEntity roleMenuEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._addEntity(roleMenuEntity));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._update(params));
    }

    public Object update(RoleMenuEntity roleMenuEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._updateEntity(roleMenuEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._deleteForce(id));
    }

    public Object queryAll(Map params) {
        List<RoleMenuEntity> roleEntities = roleMenuMapper._queryAll(params);
        return roleEntities;
    }

    public Object get(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, roleMenuMapper._get(params.get("id").toString()));
    }

    @Transactional
    public void save(List<RoleMenuEntity> list, String roleId) {
        roleMenuMapper.clearByRoleId(roleId);
        for (RoleMenuEntity roleMenuEntity : list) {
            roleMenuEntity.setId(UUID.randomUUID().toString());
            roleMenuMapper._addEntity(roleMenuEntity);
            logger.info("The save invoke success {}", roleMenuEntity.getMenuId());
        }
    }

    public List<String> queryByRoleId(String roleId) {
        return roleMenuMapper.queryMenuIdByRoleId(roleId);
    }

    public void deleteByMenuId(String id) {
        roleMenuMapper.deleteByMenuId(id);
    }
}

