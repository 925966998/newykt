package com.ky.ykt.service;

import com.ky.ykt.entity.ProjectDetailEntity;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mapper.ProjectDetailMapper;
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
public class ProjectDetailService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDetailService.class);

    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    PersonMapper personMapper;

    public Object queryAll(Map params) {
        List<ProjectDetailEntity> projectDetailEntities = projectDetailMapper._queryAll(params);
        return projectDetailEntities;
    }

    public Map<String, Object> queryOne(String id) {
        Map<String, Object> stringObjectMap = projectDetailMapper.queryById(id);
        return stringObjectMap;
    }

    public RestResult queryPage(Map params) {
        List<ProjectDetailEntity> list = projectDetailMapper._queryPage(params);
        long count = projectDetailMapper._queryCount(params);

        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);

    }

    @Transactional
    public Object update(ProjectDetailEntity projectDetailEntity) {
        personMapper.updateByProjectId(projectDetailEntity.getId());
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectDetailMapper._updateEntity(projectDetailEntity));
    }

}
