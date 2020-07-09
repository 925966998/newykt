package com.ky.ykt.service;

import com.ky.ykt.entity.AreasEntity;
import com.ky.ykt.entity.DepartmentEntity;
import com.ky.ykt.mapper.AreasCountyMapper;
import com.ky.ykt.mapper.AreasMapper;
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
 * @createTime: 2020-02-27 17:00
 * @version: v1.0
 */
@Service
public class AreasService {

    private static final Logger logger = LoggerFactory.getLogger(AreasService.class);

    @Autowired
    AreasMapper areasMapper;
  /*  @Autowired
    AreasCountyMapper areasCountyMapper;*/

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        List<AreasEntity> areasEntities = areasMapper._queryAll(params);
        return new RestResult(areasEntities.size(), areasEntities);
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<AreasEntity> list = areasMapper._queryPage(params);
        long count = areasMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._get(params.get("id")));
    }
    public AreasEntity get(String id) {
        AreasEntity areasEntity = areasMapper._get(id);
        return areasEntity;
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(AreasEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(AreasEntity AreasEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._updateEntity(AreasEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, areasMapper._deleteForce(id));
    }

    public Object queryById(String id) {
        return areasMapper._get(id);
    }

   /* public Object queryByCounty(Map params) {
        return areasCountyMapper._queryAll(params);
    }*/



    public Object queryByLevel(Map params) {
       return areasMapper._queryAll(params);
    }

    public List<AreasEntity> queryByParentId(Map<String, Object> params) {
        List<AreasEntity> areasEntities = areasMapper._queryAll(params);
        return areasEntities;
    }

    public List<AreasEntity> getByPId(String parentId) {
        List<AreasEntity> areasEntity = areasMapper.queryByPid(parentId);
        return  areasEntity;
    }
}