package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.UserProjectTypeEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.entity.UserProjectTypeEntity;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mapper.RoleMenuMapper;
import com.ky.ykt.mapper.UserProjectTypeMapper;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.UserProjectTypeService;
import com.ky.ykt.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 */
@RestController
@RequestMapping("/ky-ykt/userProjectType")
public class UserProjectTypeController {

    private static final Logger logger = LoggerFactory.getLogger(UserProjectTypeController.class);

    @Autowired
    UserProjectTypeService userProjectTypeService;
    @Autowired
    UserProjectTypeMapper userProjectTypeMapper;
    @Autowired
    MenuMapper menuMapper;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleMenuController queryByParams method params are {}", params);
        return userProjectTypeService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleMenuController queryByParams method params are {}", params);
        return userProjectTypeService.get(params);
    }

    /**
     * 新增OR更新数据
     */
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body) {
        logger.info("The RoleMenuController saveOrUpdate method params are {}", body);
        UserProjectTypeEntity UserProjectTypeEntity = JSONObject.parseObject(body, UserProjectTypeEntity.class);
        if (UserProjectTypeEntity.getId() != null && UserProjectTypeEntity.getId().length() > 0) {
            return userProjectTypeService.update(UserProjectTypeEntity);
        } else {
            UserProjectTypeEntity.setId(UUID.randomUUID().toString());
            return userProjectTypeService.add(UserProjectTypeEntity);
        }
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleMenuController delete method params is {}", params);
        return userProjectTypeService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleMenuController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                userProjectTypeService._deleteForce(split[i]);
            }
        } else {
            userProjectTypeService._deleteForce(params.get("id").toString());
        }
        return new RestResult();
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params.put("currentPage", params.get("page"));
        params.put("pageSize", params.get("rows"));
        logger.info("The RoleMenuController queryPage method params are {}", params);
        return userProjectTypeService.queryPage(params);
    }
    /*
    *//**
     * 保存权限数据
     *//*
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public void save(@RequestBody String body, HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        logger.info("The RoleMenuController save method params are {} roleId is {}", body, roleId);
        List<TreeNode> treeNodes = JSONArray.parseArray(body, TreeNode.class);
        List<UserProjectTypeEntity> list = new ArrayList<>();
        for (TreeNode treeNode : treeNodes) {
            UserProjectTypeEntity UserProjectTypeEntity = new UserProjectTypeEntity();
            UserProjectTypeEntity.setMenuId(treeNode.getId());
            UserProjectTypeEntity.setRoleId(roleId);
            list.add(UserProjectTypeEntity);
        }
        logger.info("The RoleMenuController save method params list is {}", list.size());
        userProjectTypeService.save(list, roleId);
    }
    */


}
