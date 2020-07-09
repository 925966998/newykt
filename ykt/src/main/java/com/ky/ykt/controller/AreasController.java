package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.AreasEntity;
import com.ky.ykt.entity.DepartmentEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.AreasService;
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
 * @class: ykt
 * @classDesc: 功能描述（区域划分）
 * @author: yaoWieJie
 * @createTime: 2020-02-27 16:59
 * @version: v1.0
 */
@RestController
@RequestMapping("/ky-ykt/areas")
public class AreasController {

    private static final Logger logger = LoggerFactory.getLogger(AreasController.class);

    @Autowired
    AreasService areasService;


    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController queryByParams method params are {}", params);
        return areasService.queryAll(params);
    }
    /**
     * 根据条件查询数据县城（不分页）
     */
    @SuppressWarnings("rawtypes")
   /* @RequestMapping(value = "/queryByCounty", method = RequestMethod.GET)
    public Object queryByCounty(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController queryByParams method params are {}", params);
        return areasService.queryByCounty(params);
    }*/

    @RequestMapping(value = "/queryByLevel", method = RequestMethod.GET)
    public Object queryByLevel(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController queryTowns method params are {}", params);
        return areasService.queryByLevel(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Object queryById(String id) {
        logger.info("The AreasController queryById method params are {}", id);
       return areasService.queryById(id);


    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "区域管理新增,修改操作", module = "区域管理")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(AreasEntity areasEntity) {
        logger.info("The AreasController saveOrUpdate method params are {}", areasEntity);
        if (StringUtils.isNotEmpty(areasEntity.getId())) {
            if (StringUtils.isEmpty(areasEntity.getParentId())) {
                areasEntity.setParentId("0");
            }
            return areasService.update(areasEntity);
        } else {
            areasEntity.setId(null);
            AreasEntity areasEntity1 = areasService.get(areasEntity.getParentId());
            if (StringUtils.isEmpty(areasEntity.getParentId())) {
                areasEntity.setParentId("0");
                areasEntity.setCode("01");
                areasEntity.setLevel(1);
            }else {
                areasEntity.setLevel(areasEntity1.getLevel()+1);
                areasEntity.setCode(areasEntity1.getCode()+"01");
            }
            return areasService.add(areasEntity);
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
        logger.info("The AreasController delete method params is {}", params);
        return areasService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "用户管理物理删除操作", module = "用户管理")
    @RequestMapping(value = "/deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                areasService._deleteForce(split[i]);
            }
        } else {
            areasService._deleteForce(params.get("id").toString());
        }
        return new RestResult();
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController queryPage method params are {}", params);
        RestResult restResult = areasService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }
    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "行政区域管理删除操作", module = "行政区域管理")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController delete method params is {}", params);
        return areasService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "行政区域管理删除操作", module = "行政区域管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                areasService.delete(id);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }




    /**
     * 部门树
     */
    @RequestMapping(value = "/queryByParentId", method = RequestMethod.GET)
    public Object queryByParentId(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        List<AreasEntity> areasEntities = areasService.queryByParentId(params);
        List<TreeNode> treeNodes = new ArrayList();
        for (AreasEntity areasEntity : areasEntities) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(areasEntity.getId());
            treeNode.setParentId(areasEntity.getParentId());
            treeNode.setText(areasEntity.getName());
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
}