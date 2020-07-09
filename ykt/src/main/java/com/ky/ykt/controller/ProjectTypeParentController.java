package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.ProjectTypeParentEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectTypeParentService;
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
 * @ClassName ProjectTypeParentController
 * @Description: 项目类型
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/projectTypeParent")
public class ProjectTypeParentController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTypeParentController.class);

    @Autowired
    ProjectTypeParentService projectTypeParentService;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeParentController queryByParams method params are {}", params);
        return projectTypeParentService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "项目类型新增，修改操作", module = "项目类型")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectTypeParentEntity projectTypeParentEntity) {
        logger.info("The ProjectTypeParentController saveOrUpdate method params are {}", projectTypeParentEntity);
//        ProjectTypeEntity ProjectTypeEntity = JSONObject.parseObject(body, ProjectTypeEntity.class);
        if (StringUtils.isNotEmpty(projectTypeParentEntity.getId())) {
            return projectTypeParentService.update(projectTypeParentEntity);
        } else {
            projectTypeParentEntity.setId(null);
            return projectTypeParentService.add(projectTypeParentEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeParentController queryPage method params are {}", params);
        RestResult restResult = projectTypeParentService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }


    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Object select(String id) {
        logger.info("The ProjectTypeParentController queryPage method params are {}", id);
        RestResult restResult = projectTypeParentService._get(id);
        ProjectTypeParentEntity data = (ProjectTypeParentEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "项目类型删除操作", module = "项目类型")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeParentController delete method params is {}", params);
        return projectTypeParentService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "项目类型删除操作", module = "项目类型")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeParentController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectTypeParentService.delete(id);
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
