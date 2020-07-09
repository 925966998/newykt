package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.RoleMenuEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mapper.RoleMenuMapper;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.RoleMenuService;
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
@RequestMapping("/ky-ykt/roleMenu")
public class RoleMenuController {

    private static final Logger logger = LoggerFactory.getLogger(RoleMenuController.class);

    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    RoleMenuMapper roleMenuMapper;
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
        return roleMenuService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The RoleMenuController queryByParams method params are {}", params);
        return roleMenuService.get(params);
    }

    /**
     * 新增OR更新数据
     */
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body) {
        logger.info("The RoleMenuController saveOrUpdate method params are {}", body);
        RoleMenuEntity roleMenuEntity = JSONObject.parseObject(body, RoleMenuEntity.class);
        if (roleMenuEntity.getId() != null && roleMenuEntity.getId().length() > 0) {
            return roleMenuService.update(roleMenuEntity);
        } else {
            roleMenuEntity.setId(UUID.randomUUID().toString());
            return roleMenuService.add(roleMenuEntity);
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
        return roleMenuService.delete(params.get("id").toString());
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
                roleMenuService._deleteForce(split[i]);
            }
        } else {
            roleMenuService._deleteForce(params.get("id").toString());
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
        return roleMenuService.queryPage(params);
    }

    /**
     * 保存权限数据
     */
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public void save(@RequestBody String body, HttpServletRequest request) {
        String roleId = request.getParameter("roleId");
        logger.info("The RoleMenuController save method params are {} roleId is {}", body, roleId);
        List<TreeNode> treeNodes = JSONArray.parseArray(body, TreeNode.class);
        List<RoleMenuEntity> list = new ArrayList<>();
        for (TreeNode treeNode : treeNodes) {
            RoleMenuEntity roleMenuEntity = new RoleMenuEntity();
            roleMenuEntity.setMenuId(treeNode.getId());
            roleMenuEntity.setRoleId(roleId);
            list.add(roleMenuEntity);
            /*List<TreeNode> children = treeNode.getChildren();
            if (children != null) {
                for (TreeNode treeNode1 : children) {
                    RoleMenuEntity roleMenuEntity1 = new RoleMenuEntity();
                    roleMenuEntity1.setMenuId(treeNode1.getId());
                    roleMenuEntity1.setRoleId(roleId);
                    list.add(roleMenuEntity1);
                }
            }*/
        }
        logger.info("The RoleMenuController save method params list is {}", list.size());
        roleMenuService.save(list, roleId);
    }

}
