package com.ky.ykt.service;

import com.ky.ykt.entity.PersonEntity;
import com.ky.ykt.entity.ProjectDetailEntity;
import com.ky.ykt.entity.ProjectEntity;
import com.ky.ykt.entity.ProjectSourceEntity;
import com.ky.ykt.mapper.PersonMapper;
import com.ky.ykt.mapper.ProjectDetailMapper;
import com.ky.ykt.mapper.ProjectMapper;
import com.ky.ykt.mapper.ProjectSourceMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectSourceMapper projectSourceMapper;

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
        List<ProjectDetailEntity> list1 = new ArrayList<>();
        for (ProjectDetailEntity projectDetailEntity : list) {
            List<PersonEntity> personEntities = personMapper.queryProjectId1(projectDetailEntity.getId());
            BigDecimal zero = BigDecimal.ZERO;
            if (personEntities.size() > 0 && personEntities != null) {
                for (int i = 0; i < personEntities.size(); i++) {
                    /*if (personEntities.get(i).getStatus().equals("0") || personEntities.get(i).getStatus().equals("4")) {*/
                        zero = zero.add(new BigDecimal(personEntities.get(i).getGrantAmount()));
                   /* }*/
                }
            }
            //统计成功的
            ProjectEntity projectEntity = projectMapper._get(projectDetailEntity.getProjectId());
            ProjectSourceEntity projectSourceEntity = projectSourceMapper._get(projectEntity.getProjectSourceId());
            BigDecimal bigDecimal = projectMapper.querySuccess(projectEntity.getProjectSourceId());
            projectDetailEntity.setSurplusAmount(projectSourceEntity.getTotalAmount().subtract(isNullBig(bigDecimal)));
            projectDetailEntity.setPaymentAmount(zero);
            //projectDetailEntity.setSurplusAmount(projectDetailEntity.getSurplusAmount());
            list1.add(projectDetailEntity);
        }

        long count = projectDetailMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list1, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);

    }

    @Transactional
    public Object update(ProjectDetailEntity projectDetailEntity) {
        personMapper.updateByProjectId(projectDetailEntity.getId());
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectDetailMapper._updateEntity(projectDetailEntity));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectDetailMapper._deleteForce(id));
    }

    public BigDecimal isNullBig(BigDecimal b) {
        if (b == null) {
            return BigDecimal.ZERO;
        }
        return b;
    }

}
