package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.RoleMenuEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.entity.UserProjectTypeEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.SysUserMapper;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.SysUserService;
import com.ky.ykt.service.UserProjectTypeService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
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
 * 系统人员模块
 */
@RestController
@RequestMapping("/ky-ykt/sysUser")
public class SysUserController {

    private static final Logger logger = LoggerFactory.getLogger(SysUserController.class);

    @Autowired
    SysUserService sysUserService;
    @Autowired
    UserProjectTypeService userProjectTypeService;
    @Autowired
    SysUserMapper sysUserMapper;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The SysUserController queryByParams method params are {}", params);
        return sysUserService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The SysUserController queryByParams method params are {}", params);
        return sysUserService.queryById(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "用户管理新增,修改操作", module = "用户管理")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, consumes = "application/json")
    public Object saveOrUpdate(@RequestBody String body) {
        logger.info("The SysUserController saveOrUpdate method params are {}", body);
        SysUserEntity sysUserEntity = JSONObject.parseObject(body, SysUserEntity.class);
        if (StringUtils.isNotEmpty(sysUserEntity.getId())) {
            SysUserEntity sysUserEntity1 = sysUserMapper._get(sysUserEntity.getId());
            sysUserEntity.setPassword(sysUserEntity1.getPassword());
            return sysUserService.update(sysUserEntity);
        } else {
            if (!StringUtils.isEmpty(sysUserEntity.getIdCardNo())) {
                String idCardNo = sysUserEntity.getIdCardNo();
                sysUserEntity.setPassword(DigestUtils.md5DigestAsHex((idCardNo.substring(idCardNo.length() - 6, idCardNo.length())).getBytes()));
            } else {
                sysUserEntity.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
            }
            sysUserEntity.setId(UUID.randomUUID().toString());
            sysUserEntity.setStatus(0);
            SysUserEntity sysUserEntity1 = sysUserMapper.queryByUserName(sysUserEntity.getUserName());
            if (sysUserEntity1 != null) {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "用户名已存在");
            }
            return sysUserService.add(sysUserEntity);
        }
    }

    @Log(description = "用户修改密码操作", module = "用户管理")
    @RequestMapping(value = "/updatePass", method = RequestMethod.GET)
    public Object updatePass(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The SysUserController updatePass method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        SysUserEntity sysUserEntity = sysUserMapper._get(user.getId());
        String oldPass = params.get("oldPass").toString();
        String newPass = params.get("newPass").toString();
        String newPassCheck = params.get("newPassCheck").toString();
        if (DigestUtils.md5DigestAsHex(oldPass.getBytes()).equals(sysUserEntity.getPassword())) {
            if (newPass.equals(newPassCheck)) {
                sysUserEntity.setPassword(DigestUtils.md5DigestAsHex(newPass.getBytes()));
                sysUserService.update(sysUserEntity);
                return new RestResult(1, "修改成功");
            } else {
                return new RestResult(2, "密码不一致，请重新填写");
            }

        } else {
            return new RestResult(3, "原始密码错误");
        }
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "用户管理逻辑删除操作", module = "用户管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The SysUserController delete method params is {}", params);
        return sysUserService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "用户管理物理删除操作", module = "用户管理")
    @RequestMapping(value = "/deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The SysUserController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                sysUserService._deleteForce(split[i]);
            }
        } else {
            sysUserService._deleteForce(params.get("id").toString());
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
        logger.info("The SysUserController queryPage method params are {}", params);
        return sysUserService.queryPage(params);
    }

    @RequestMapping(value = "/doQueryToDo", method = RequestMethod.GET)
    public Object doQueryToDo(HttpServletRequest request) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        return sysUserService.doQueryToDo(user);
    }

    /**
     * 保存权限数据
     */
    @RequestMapping(value = "saveUserProject", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public void saveUserProject(@RequestBody String body, HttpServletRequest request) {
        String userId = request.getParameter("userId");
        logger.info("The RoleMenuController save method params are {} roleId is {}", body, userId);
        List<TreeNode> treeNodes = JSONArray.parseArray(body, TreeNode.class);
        List<UserProjectTypeEntity> list = new ArrayList<>();
        for (TreeNode treeNode : treeNodes) {
            UserProjectTypeEntity userProjectTypeEntity = new UserProjectTypeEntity();
            userProjectTypeEntity.setProjectTypeId(treeNode.getId());
            userProjectTypeEntity.setUserId(userId);
            list.add(userProjectTypeEntity);
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
        userProjectTypeService.save(list, userId);
    }
}
