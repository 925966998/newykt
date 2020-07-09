package com.ky.ykt.service;

import com.ky.ykt.entity.PersonReplacementEntity;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mapper.PersonReplacementMapper;
import com.ky.ykt.mapper.PersonUploadMapper;
import com.ky.ykt.mybatis.RestResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-10 8:38
 * @version: v1.0
 */
@Service
public class PersonReplacementService {

    private static final Logger logger = LoggerFactory.getLogger(PersonReplacementService.class);
    
    @Autowired
    PersonReplacementMapper personReplacementMapper;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    PersonUploadMapper personUploadMapper;

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        List<PersonReplacementEntity> areasEntities = personReplacementMapper._queryAll(params);
        return new RestResult(areasEntities.size(),areasEntities);
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<PersonReplacementEntity> list = personReplacementMapper._queryPage(params);
        long count = personReplacementMapper._queryCount(params);
        return new RestResult(count,list);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(PersonReplacementEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(PersonReplacementEntity PersonReplacementEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._updateEntity(PersonReplacementEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        int i = 0;
        try{
            PersonReplacementEntity personReplacementEntity = personReplacementMapper._get(id);
            personMapper._delete(personReplacementEntity.getPersonId());
            i = personReplacementMapper._deleteForce(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, i);
    }

    public Object queryById(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personReplacementMapper._get(params.get("id")));
    }

    public PersonReplacementEntity queryReplacementById(Map params) {
        return personReplacementMapper.queryReplacementById(params);
    }

    public List<PersonReplacementEntity> queryReplacementBypersonId(Map params) {
        return personReplacementMapper.queryReplacementBypersonId(params);
    }
}