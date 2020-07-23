package com.ky.ykt.service;

import com.ky.ykt.entity.UserProjectTypeEntity;
import com.ky.ykt.entity.UserProjectTypeEntity;
import com.ky.ykt.mapper.UserProjectTypeMapper;
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
public class UserProjectTypeService {
    private static final Logger logger = LoggerFactory.getLogger(UserProjectTypeService.class);

    @Autowired
    UserProjectTypeMapper userProjectTypeMapper;

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<UserProjectTypeEntity> list = userProjectTypeMapper._queryPage(params);
        long count = userProjectTypeMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public RestResult _get(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._get(id));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._add(params));
    }

    public Object add(UserProjectTypeEntity userProjectTypeEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._addEntity(userProjectTypeEntity));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._update(params));
    }

    public Object update(UserProjectTypeEntity userProjectTypeEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._updateEntity(userProjectTypeEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._deleteForce(id));
    }

    public Object queryAll(Map params) {
        List<UserProjectTypeEntity> roleEntities = userProjectTypeMapper._queryAll(params);
        return roleEntities;
    }

    public Object get(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userProjectTypeMapper._get(params.get("id").toString()));
    }

    @Transactional
    public void save(List<UserProjectTypeEntity> list, String userId) {
        userProjectTypeMapper.clearByUserId(userId);
        for (UserProjectTypeEntity userProjectTypeEntity : list) {
            userProjectTypeEntity.setId(UUID.randomUUID().toString());
            userProjectTypeMapper._addEntity(userProjectTypeEntity);
            logger.info("The save invoke success {}", userProjectTypeEntity.getProjectTypeId());
        }
    }

    public List<String> queryByprojectTypeId(String UserId) {
        return userProjectTypeMapper.queryByprojectTypeId(UserId);
    }

    public void deleteByUserId(String userId) {
        userProjectTypeMapper.deleteByUserId(userId);
    }

}

