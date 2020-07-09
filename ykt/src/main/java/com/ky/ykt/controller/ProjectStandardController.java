package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.ProjectStandardEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectStandardService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName ProjectStandardController
 * @Description: 部门管理
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/projectStandard")
public class ProjectStandardController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectStandardController.class);

    @Autowired
    ProjectStandardService projectStandardService;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectStandardController queryByParams method params are {}", params);
        return projectStandardService.queryAll(params);
    }

    @RequestMapping(value = "queryByParamsSearch", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParamsSearch(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectStandardController queryByParamsSearch method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("parentId", user.getDepartmentId());
        return projectStandardService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "部门管理新增，修改操作", module = "部门管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectStandardEntity projectStandardEntity) {
        logger.info("The ProjectStandardController saveOrUpdate method params are {}", projectStandardEntity);
//        projectStandardEntity projectStandardEntity = JSONObject.parseObject(body, projectStandardEntity.class);
        if (StringUtils.isNotEmpty(projectStandardEntity.getId())) {

            return projectStandardService.update(projectStandardEntity);
        } else {
            projectStandardEntity.setId(null);

        }
        return projectStandardService.add(projectStandardEntity);
    }


    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectStandardController queryPage method params are {}", params);
        RestResult restResult = projectStandardService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }


    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Object select(String id) {
        logger.info("The ProjectStandardController queryPage method params are {}", id);
        RestResult restResult = projectStandardService._get(id);
        ProjectStandardEntity data = (ProjectStandardEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "部门管理删除操作", module = "部门管理")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectStandardController delete method params is {}", params);
        return projectStandardService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "部门管理删除操作", module = "部门管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectStandardController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectStandardService.delete(id);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }


    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }
}
