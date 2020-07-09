package com.ky.ykt.service;

import com.ky.ykt.entity.AreasEntity;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.mapper.AreasMapper;
import com.ky.ykt.mapper.PersonUploadMapper;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-02-29 11:33
 * @version: v1.0
 */
@Service
public class PersonUploadService {
    @Autowired
    PersonUploadMapper personUploadMapper;

    @Autowired
    AreasMapper areasMapper;
    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public RestResult queryAll(Map params) {
        List<PersonUploadEntity> quotaEntities = personUploadMapper._queryAll(params);
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, quotaEntities);
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public Object queryPage(Map params) {
        if (StringUtils.isNotBlank(MapUtils.getString(params, "areaId"))) {
            String areaId = params.get("areaId").toString();
            AreasEntity areasEntity = areasMapper._get(areaId);
            params.put("level", areasEntity.getLevel());
//            List<AreasEntity> areasEntities = areasMapper.queryByPid(areaId);
//            List<String> areaIdList = new ArrayList<String>();
//            if (areasEntities != null && areasEntities.size() > 0) {
//                for (AreasEntity areasEntity1 : areasEntities
//                ) {
//                    areaIdList.add(areasEntity1.getId());
//                }
//                areaIdList.add(areaId);
//                params.put("areaIdList", areaIdList);
//            }
        }

        List<PersonUploadEntity> list = personUploadMapper._queryPage(params);
        long count = personUploadMapper._queryCount(params);
        return new RestResult(count, list);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._get(params.get("id")));
    }

    public Object queryById(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper.queryById(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._add(params));
    }

    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(PersonUploadEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._addEntity(entity));
    }


    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._update(params));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(PersonUploadEntity personUploadEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._updateEntity(personUploadEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper._deleteForce(id));
    }

    public Object doSubmitAudit(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper.doSubmitAudit(id));
    }

    public Object push(String id) {
        PersonUploadEntity personUploadEntity = personUploadMapper._get(id);

        return personUploadMapper._updateEntity(personUploadEntity);
    }

    public Object audit(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper.audit(params));
    }

    public long queryCount(Map<String, Object> params) {
        return personUploadMapper._queryCount(params);
    }

    public Object queryByAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, personUploadMapper.queryByAll(params));
    }

    public Object queryWechatList(Map params) {
        List<PersonUploadEntity> list = personUploadMapper._queryPage(params);
        long count = personUploadMapper._queryCount(params);
        RestResult restResult = new RestResult(count, list);
        return restResult.getRows();
    }
}