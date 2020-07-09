package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.MenuService;
import com.ky.ykt.service.RoleMenuService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/ky-ykt/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    MenuService menuService;
    @Autowired
    RoleMenuService roleMenuService;
    @Autowired
    MenuMapper menuMapper;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryByParams method params are {}", params);
        return menuService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryByParams method params are {}", params);
        return menuService.get(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "菜单管理新增，修改操作", module = "菜单管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body) {
        logger.info("The PersonController saveOrUpdate method params are {}", body);
        MenuEntity menuEntity = JSONObject.parseObject(body, MenuEntity.class);
        if (menuEntity.getIsFirstMenu() == 1) {
            menuEntity.setMenuUrl("");
            menuEntity.setParentId("");
        }
        if (menuEntity.getId() != null && menuEntity.getId().length() > 0) {
            return menuService.update(menuEntity);
        } else {
            menuEntity.setId(UUID.randomUUID().toString());
            MenuEntity menuEntity1 = menuService.queryByMenuName(menuEntity.getMenuName());
            if (menuEntity1 != null) {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该菜单已存在");
            }
            return menuService.add(menuEntity);
        }
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "菜单管理逻辑删除操作", module = "菜单管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController delete method params is {}", params);
        return menuService.delete(params.get("id").toString());
    }

    @RequestMapping(value = "/queryCurMostSort", method = RequestMethod.GET)
    public Object queryCurMostSort(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController queryCurMostSort method params is {}", params);
        return menuService.queryCurMostSort(params);
    }

    /**
     * 物理删除
     */
    @Log(description = "菜单管理物理删除操作", module = "菜单管理")
    @RequestMapping(value = "deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The PersonController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                menuService._deleteForce(split[i]);
                roleMenuService.deleteByMenuId(split[i]);
            }
        } else {
            menuService._deleteForce(params.get("id").toString());
            roleMenuService.deleteByMenuId(params.get("id").toString());
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
        logger.info("The PersonController queryPage method params are {}", params);
        return menuService.queryPage(params);
    }

    @RequestMapping(value = "/menuTree/{roleId}", method = RequestMethod.GET)
    public Object menuTree(HttpServletRequest request, @PathVariable String roleId) {
        Map params = HttpUtils.getParams(request);

        List<MenuEntity> menuEntities = menuService.queryAll(params);
        List<String> roleIds = new ArrayList<>();
        if (roleId != null) {
            roleIds = roleMenuService.queryByRoleId(roleId);
        }
        List<TreeNode> treeNodes = new ArrayList();
        for (MenuEntity menuEntity : menuEntities) {
            TreeNode treeNode = new TreeNode();
            List<TreeNode> children = new ArrayList();
            treeNode.setId(menuEntity.getId());
            treeNode.setParentId(menuEntity.getParentId());
            treeNode.setText(menuEntity.getMenuName());
            List<MenuEntity> menuEntities1 = menuService.queryByPid(menuEntity.getId());
            if (roleIds.size() > 0) {
                if (StringUtils.isEmpty(menuEntity.getParentId())) {
                    //返回时父节点不需要设置Checked如有有子节点默认为模糊选中
                    /*if (roleIds.contains(menuEntity.getId())) {
                        treeNode.setChecked(true);
                    } else {
                        treeNode.setChecked(false);
                    }*/

                    for (MenuEntity menuEntity1 : menuEntities1) {
                        TreeNode childTreeNode = new TreeNode();
                        if (roleIds.contains(menuEntity1.getId())) {
                            childTreeNode.setChecked(true);
                        } else {
                            childTreeNode.setChecked(false);
                        }
                        childTreeNode.setId(menuEntity1.getId());
                        childTreeNode.setParentId(menuEntity1.getParentId());
                        childTreeNode.setText(menuEntity1.getMenuName());
                        children.add(childTreeNode);
                    }
                    treeNode.setChildren(children);
                    treeNodes.add(treeNode);
                }
            } else {
                if (StringUtils.isEmpty(menuEntity.getParentId())) {
                    for (MenuEntity menuEntity1 : menuEntities1) {
                        TreeNode childTreeNode = new TreeNode();
                        childTreeNode.setId(menuEntity1.getId());
                        childTreeNode.setParentId(menuEntity1.getParentId());
                        childTreeNode.setText(menuEntity1.getMenuName());
                        children.add(childTreeNode);
                    }
                    treeNode.setChildren(children);
                    treeNodes.add(treeNode);
                }
            }
        }
        logger.info("The convert treeNode is {}", JSON.toJSONString(treeNodes));
        return treeNodes;
    }
}
