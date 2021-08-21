package com.ky.ykt.service;

import com.ky.ykt.entity.*;
import com.ky.ykt.mapper.*;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectService
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    ProjectDetailMapper projectDetailMapper;
    @Autowired
    PersonMapper personMapper;
    @Autowired
    ProjectAreaMapper projectAreaMapper;

    public Object queryAll(Map params, HttpServletRequest request) {
        Object roleCodeSession = request.getSession().getAttribute("roleCode");
        String roleCode = "";
        if (roleCodeSession != null) {
            roleCode = roleCodeSession.toString();
            if (roleCode.equals("4")) {
                SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
                DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                params.put("DJFlag", "4J");
                params.put("departmentId", departmentEntity.getParentId());
                params.put("userId",user.getId());
            }
        }
        List<ProjectEntity> projectDetailEntities = projectMapper._queryAll(params);
        return projectDetailEntities;
    }

    public Map<String, Object> queryCount(Map params) {
        params.put("state", 1);
        long finishState = projectMapper._queryCount(params);
        params.put("state", 0);
        long ingState = projectMapper._queryCount(params);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("finishState", finishState);
        map.put("ingState", ingState);
        return map;
    }

    public RestResult queryPage(Map params) {
        List<ProjectEntity> list = projectMapper._queryPage(params);
        for (ProjectEntity projectEntity :list) {
            projectEntity.setSurplusAmount(isNullBig(projectEntity.getSurplusAmount()));
            projectEntity.setTotalAmount(isNullBig(projectEntity.getTotalAmount()));
            projectEntity.setPaymentAmount(isNullBig(projectEntity.getPaymentAmount()));
            projectEntity.setCountyAmount(isNullBig(projectEntity.getCountyAmount()));
            projectEntity.setCityAmount(isNullBig(projectEntity.getCityAmount()));
            projectEntity.setProvinceAmount(isNullBig(projectEntity.getProvinceAmount()));
            projectEntity.setCenterAmount(isNullBig(projectEntity.getCenterAmount()));

        }
        long count = projectMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public Object add(ProjectEntity projectEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectMapper._addEntity(projectEntity));
    }

    public Map<String, Object> selectHomeNum(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (!user.getUserName().equals("admin")) {
            List<DepartmentEntity> departmentEntities = departmentMapper.queryByParentId(user.getDepartmentId());
            List<String> departmentIdList = new ArrayList<String>();
            if (departmentEntities != null && departmentEntities.size() > 0) {
                for (DepartmentEntity departmentEntity : departmentEntities
                ) {
                    departmentIdList.add(departmentEntity.getId());
                }
                departmentIdList.add(user.getDepartmentId());
                params.put("departmentIdList", departmentIdList);
                params.put("departmentIdListFlag", "departmentIdListFlag");
            }
        }
        params.put("state", 0);
        params.put("flag", 1);
        long countdiv1 = projectDetailMapper._queryCount(params);
        params.put("state", 1);
        params.put("flag", 1);
        long countdiv2 = projectDetailMapper._queryCount(params);
        params.put("status", 2);
        long countdiv4 = personMapper._queryCount(params);
        map.put("div1", countdiv1);
        map.put("div2", countdiv2);
        map.put("div4", countdiv4);
        Object roleCodeSession = request.getSession().getAttribute("roleCode");
        String roleCode = "";
        params = new HashMap<>();
        if (roleCodeSession != null) {
            roleCode = roleCodeSession.toString();
            if (roleCode.equals("4")) {
                DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                params.put("DJFlag", "4J");
                params.put("departmentId", departmentEntity.getParentId());
            }
        }
        params.put("flag", "2");
        params.put("state", 0);
        List<ProjectEntity> projectDetailEntities = projectMapper._queryAll(params);
        map.put("div3", projectDetailEntities.size());
        return map;
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectMapper._delete(id));
    }

    public List<StatisticEntity> queryType(Map params) {
        return projectMapper.queryType(params);
    }

    public List<StatisticEntity> statistic(Map pagerParam) {
        return projectDetailMapper.statistic(pagerParam);
    }

    public RestResult _get(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectMapper._get(id));
    }

    public Object update(ProjectEntity projectEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, projectMapper._updateEntity(projectEntity));
    }

    public Object queryAllProject(Map params, HttpServletRequest request) {
        /*
        Object roleCodeSession = request.getSession().getAttribute("roleCode");
        String roleCode = "";
        if (roleCodeSession != null) {
            roleCode = roleCodeSession.toString();
            if (roleCode.equals("4")) {
                SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
                DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                params.put("DJFlag", "4J");
                params.put("departmentId", departmentEntity.getParentId());
            }
        }
        */
        List<ProjectEntity> projectDetailEntities = projectMapper._queryAll(params);
        return projectDetailEntities;
    }

    public RestResult queryMetionPage(Map params) {
        List<ProjectAreaEntity> list = projectAreaMapper._queryPage(params);
        long count = projectAreaMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    public BigDecimal isNullBig(BigDecimal b) {
        if (b == null) {
            return BigDecimal.ZERO;
        }
        return b;
    }

    public Object queryProject(Map params, HttpServletRequest request) {
                SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
                if(!user.getRoleId().equals("a599f1da-f57c-4afc-a600-b58e15836aa0")){
                    DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
                    params.put("departmentId", departmentEntity.getParentId());
                    params.put("userId",user.getId());
                }
        List<ProjectEntity> projectDetailEntities = projectMapper.queryProject(params);
        return projectDetailEntities;
    }

    public Object queryFFproject(Map params, HttpServletRequest request) {
        List<ProjectEntity> projectDetailEntities = projectMapper.queryFFproject(params);
        return projectDetailEntities;
    }
}
