package com.ky.ykt.service;

import com.ky.ykt.entity.PersonEntity;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PersonService {

    @Autowired
    PersonMapper personMapper;

    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        List<PersonEntity> quotaEntities = personMapper._queryAll(params);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, quotaEntities);
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        List<PersonEntity> list = personMapper._queryPage(params);
        long count = personMapper._queryCount(params);
        return new RestResult(count, list);
    }


    public Object queryById(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(PersonEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(PersonEntity PersonEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._updateEntity(PersonEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper._deleteForce(id));
    }

    public Object doSubmitAudit(String id, String projectId) {

        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper.doSubmitAudit(id, projectId));
    }

    public Object push(String id) {
        PersonEntity personEntity = personMapper._get(id);
        return personMapper._updateEntity(personEntity);
    }

    public Object audit(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper.audit(params));
    }

    public long queryCount(Map<String, Object> params) {
        return personMapper._queryCount(params);
    }

    public long queryCountByProjectCode(String id) {
        return personMapper.queryCountByProjectCode(id);
    }

    public PersonEntity get(String id) {
        return personMapper._get(id);
    }

    public void submitToBuss(String id) {
        personMapper.submitToBuss(id);
    }

    public Object queryByAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper.queryByAll(params));
    }

    public Object queryByPage(Map params) {
        List<PersonEntity> list = personMapper._queryByPage(params);
        long count = personMapper._queryCount(params);
        return new RestResult(count, list);
    }

    public RestResult queryWechatPerson(Map params) {
        //return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personMapper.queryWechatPerson(params));
        List<PersonEntity> list = personMapper.queryWechatPerson(params);
        long count = personMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public PersonUploadEntity queryPerson(Map params) {
        return personMapper.queryPerson(params);
    }

    @Transactional
    public RestResult reloadPull(Map params) {
        params.put("status", "1");
        params.put("flag", "2");
        List<PersonEntity> personEntities = personMapper._queryAll(params);
        if (personEntities != null && personEntities.size() > 0) {
            for (PersonEntity personEntity : personEntities
            ) {
                personEntity.setId(UUID.randomUUID().toString());
                personEntity.setStatus("3");
                personEntity.setItemId(params.get("newProjectId").toString());
                //获取当前操作人信息
                personMapper._addEntity(personEntity);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }
}