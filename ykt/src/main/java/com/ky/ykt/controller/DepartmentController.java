package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.DepartmentEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.DepartmentService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName DepartmentController
 * @Description: 部门管理
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/department")
public class DepartmentController {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

    @Autowired
    DepartmentService departmentService;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController queryByParams method params are {}", params);
        return departmentService.queryAll(params);
    }

    @RequestMapping(value = "queryByParamsSearch", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParamsSearch(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController queryByParamsSearch method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        params.put("parentId",user.getDepartmentId());
        return departmentService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "部门管理新增，修改操作", module = "部门管理")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(DepartmentEntity departmentEntity) {
        logger.info("The DepartmentController saveOrUpdate method params are {}", departmentEntity);
//        DepartmentEntity departmentEntity = JSONObject.parseObject(body, DepartmentEntity.class);
        if (StringUtils.isNotEmpty(departmentEntity.getId())) {
            if (StringUtils.isEmpty(departmentEntity.getParentId())) {
                departmentEntity.setParentId("0");
            }
            return departmentService.update(departmentEntity);
        } else {
            departmentEntity.setId(null);
            if (StringUtils.isEmpty(departmentEntity.getParentId())) {
                departmentEntity.setParentId("0");
            }
            return departmentService.add(departmentEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController queryPage method params are {}", params);
        RestResult restResult = departmentService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    /**
     * 部门树
     */
    @RequestMapping(value = "/queryByParentId", method = RequestMethod.GET)
    public Object queryByParentId(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<DepartmentEntity> departmentEntities = departmentService.queryByParentId(params);
        List<TreeNode> treeNodes = new ArrayList();
        for (DepartmentEntity departmentEntity : departmentEntities) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(departmentEntity.getId());
            treeNode.setParentId(departmentEntity.getParentId());
            treeNode.setText(departmentEntity.getDepartmentName());
            treeNodes.add(treeNode);
        }
        Map<String, List<TreeNode>> sub = treeNodes.stream().filter(node -> (!node.getParentId().equals("0")) && node.getParentId() != null).collect(Collectors.groupingBy(node -> node.getParentId()));
        treeNodes.forEach(node -> node.setChildren(sub.get(node.getId())));
        List<TreeNode> collect = treeNodes.stream().filter(node -> (node.getParentId().equals("0") || node.getParentId() == null)).collect(Collectors.toList());

        TreeNode treeNodeAll = new TreeNode();
        treeNodeAll.setText("全部");
        treeNodeAll.setChildren(collect);
        treeNodes = new ArrayList<>();
        treeNodes.add(treeNodeAll);
        return treeNodes;
    }
    //            if (StringUtils.isEmpty(departmentEntity.getParentId())||departmentEntity.getParentId().equals("0")) {
//                params.put("parentId", departmentEntity.getId());
//                List<DepartmentEntity> departmentEntities1 = departmentService.queryByParentId(params);
//                for (DepartmentEntity departmentEntity1 : departmentEntities1) {
//                    TreeNode childTreeNode = new TreeNode();
//                    childTreeNode.setId(departmentEntity1.getId());
//                    childTreeNode.setParentId(departmentEntity1.getParentId());
//                    childTreeNode.setText(departmentEntity1.getDepartmentName());
//                    children.add(childTreeNode);
//                }
//                treeNode.setChildren(children);

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Object select(String id) {
        logger.info("The DepartmentController queryPage method params are {}", id);
        RestResult restResult = departmentService._get(id);
        DepartmentEntity data = (DepartmentEntity) restResult.getData();
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
        logger.info("The DepartmentController delete method params is {}", params);
        return departmentService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "部门管理删除操作", module = "部门管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                departmentService.delete(id);
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
