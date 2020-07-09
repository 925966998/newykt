package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.RoleEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.RoleService;
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
import java.util.UUID;

/**
 * @ClassName RoleController
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/19
 **/
@RestController
@RequestMapping("/ky-ykt/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    RoleService roleService;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleController queryByParams method params are {}", params);
        return roleService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "角色管理新增，修改操作", module = "角色管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object saveOrUpdate(RoleEntity roleEntity) {
        logger.info("The DepartmentController saveOrUpdate method params are {}", roleEntity);
        if (StringUtils.isNotEmpty(roleEntity.getId())) {
            return roleService.update(roleEntity);
        } else {
            roleEntity.setId(UUID.randomUUID().toString());
            return roleService.add(roleEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController queryPage method params are {}", params);
        RestResult restResult = roleService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public RoleEntity select(String id) {
        logger.info("The DepartmentController queryPage method params are {}", id);
        RestResult restResult = roleService._get(id);
        RoleEntity data = (RoleEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "角色管理删除操作", module = "角色管理")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleController delete method params is {}", params);
        return roleService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "角色管理删除操作", module = "角色管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                roleService.delete(id);
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
