package com.ky.ykt.service;

import com.ky.ykt.entity.ProjectReplacementEntity;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mapper.ProjectReplacementMapper;
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
 * @ClassName ProjectDetailService
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@Service
public class ProjectReplacementService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectReplacementService.class);

    @Autowired
    ProjectReplacementMapper projectReplacementMapper;
    @Autowired
    PersonMapper personMapper;

    public Object queryAll(Map params) {
        List<ProjectReplacementEntity> projectReplacementEntities = projectReplacementMapper._queryAll(params);
        return projectReplacementEntities;
    }

    public Map<String, Object> queryOne(String id) {
        Map<String, Object> stringObjectMap = projectReplacementMapper.queryById(id);
        return stringObjectMap;
    }

    public RestResult queryPage(Map params) {
        List<ProjectReplacementEntity> list = projectReplacementMapper._queryPage(params);
        long count = projectReplacementMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);

    }

    @Transactional
    public Object update(ProjectReplacementEntity ProjectReplacementEntity) {
        personMapper.updateByProjectId(ProjectReplacementEntity.getId());
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectReplacementMapper._updateEntity(ProjectReplacementEntity));
    }

}
