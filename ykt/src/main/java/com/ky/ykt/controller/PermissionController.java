package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.PermissionEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PermissionService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @class: ykt
 * @classDesc: 功能描述（权限管理）
 * @author: yaoWieJie
 * @createTime: 2020-02-25 16:18
 * @version: v1.0
 */
@RestController
@RequestMapping("/ky-ykt/permission")
public class PermissionController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);
    @Autowired
    PermissionService permissionService;



    /**
     * 查询当前用户的权限
     */
    @Log(description = "权限管理查询操作(不分页)", module = "权限管理")
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByUser(HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        logger.info("The DepartmentController queryByParams method params are {}", userId);
        return permissionService.queryByUser(userId);
    }

    /**
     * 分页查询
     */
    @Log(description = "权限管理查询/修改操作", module = "权限管理")
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PermissionController queryPage method params are {}", params);
        RestResult restResult = permissionService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }


    /**
     * 权限树
     */
    @RequestMapping(value = "/queryByParentId", method = RequestMethod.GET,produces = "application/json;UTF-8")
    public Object queryByParentId(HttpServletRequest request) {
        String parentId = (String) request.getSession().getAttribute("id");
        logger.info("The DepartmentController queryByParams method params are {}", parentId);
        List<PermissionEntity> permissionEntities = permissionService.queryByParentId("parentId");
        String s = JSON.toJSONString(permissionEntities);
        return s;
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "权限管理管理新增，修改操作", module = "权限管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(PermissionEntity permissionEntity) {
        logger.info("The PermissionController saveOrUpdate method params are {}", permissionEntity);
        if (StringUtils.isNotEmpty(permissionEntity.getId())) {
            return permissionService.update(permissionEntity);
        } else {
            permissionEntity.setId(null);
            return permissionService.add(permissionEntity);
        }
    }

    /**
     * 删除多个
     */
    @Log(description = "权限管理删除操作", module = "权限管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                permissionService.delete(id);
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