package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.entity.ProjectTypeEntity;
import com.ky.ykt.entity.TreeNode;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectTypeService;
import com.ky.ykt.service.UserProjectTypeService;
import com.ky.ykt.utils.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProjectTypeController
 * @Description: 资金类型
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/projectType")
public class ProjectTypeController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTypeController.class);

    @Autowired
    ProjectTypeService projectTypeService;

    @Autowired
    UserProjectTypeService userProjectTypeService;
    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeController queryByParams method params are {}", params);
        return projectTypeService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "资金类型新增，修改操作", module = "资金类型")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectTypeEntity projectTypeEntity) {
        logger.info("The ProjectTypeController saveOrUpdate method params are {}", projectTypeEntity);
//        ProjectTypeEntity ProjectTypeEntity = JSONObject.parseObject(body, ProjectTypeEntity.class);
        if (StringUtils.isNotEmpty(projectTypeEntity.getId())) {
            return projectTypeService.update(projectTypeEntity);
        } else {
            projectTypeEntity.setId(null);
            return projectTypeService.add(projectTypeEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeController queryPage method params are {}", params);
        RestResult restResult = projectTypeService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }


    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Object select(String id) {
        logger.info("The ProjectTypeController queryPage method params are {}", id);
        RestResult restResult = projectTypeService._get(id);
        ProjectTypeEntity data = (ProjectTypeEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "资金类型删除操作", module = "资金类型")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeController delete method params is {}", params);
        return projectTypeService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "资金类型删除操作", module = "资金类型")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectTypeController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectTypeService.delete(id);
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

    @RequestMapping(value = "/queryProjectTree/{userId}", method = RequestMethod.GET)
    public Object queryProjectTree(HttpServletRequest request, @PathVariable String userId) {
        Map params = HttpUtils.getParams(request);
        //找见所有项目类型
        List<ProjectTypeEntity> projectTypeEntities = projectTypeService.queryAll(params);
        List<String> ProjectTypeIds = new ArrayList<>();
        if (userId != null) {
            ProjectTypeIds = userProjectTypeService.queryByprojectTypeId(userId);
        }
        //树
        List<TreeNode> treeNodes = new ArrayList();
        for (ProjectTypeEntity projectTypeEntity : projectTypeEntities) {
            TreeNode treeNode = new TreeNode();
            List<TreeNode> children = new ArrayList();
            treeNode.setId(projectTypeEntity.getId());
            treeNode.setParentId(projectTypeEntity.getParentId());
            treeNode.setText(projectTypeEntity.getName());
            List<ProjectTypeEntity> projectTypeEntities1 = projectTypeService.queryByProjectTypeId(projectTypeEntity.getId());
            if (ProjectTypeIds.size() > 0) {
                if (StringUtils.isEmpty(projectTypeEntity.getParentId())) {
                    //返回时父节点不需要设置Checked如有有子节点默认为模糊选中
                    /*if (roleIds.contains(menuEntity.getId())) {
                        treeNode.setChecked(true);
                    } else {
                        treeNode.setChecked(false);
                    }*/
                    for (ProjectTypeEntity projectTypeEntity1 : projectTypeEntities1) {
                        if (ProjectTypeIds.contains(projectTypeEntity1.getId())) {
                            treeNode.setChecked(true);
                        } else {
                            treeNode.setChecked(false);
                        }
                    }
                    treeNodes.add(treeNode);
                }
            } else {
                if (StringUtils.isEmpty(projectTypeEntity.getParentId())) {
                    treeNodes.add(treeNode);
                }
            }
        }
        logger.info("The convert treeNode is {}", JSON.toJSONString(treeNodes));
        return treeNodes;
    }
}
