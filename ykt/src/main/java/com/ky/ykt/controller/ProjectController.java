package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.DepartmentEntity;
import com.ky.ykt.entity.ProjectAreaEntity;
import com.ky.ykt.entity.ProjectEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mapper.DepartmentMapper;
import com.ky.ykt.mapper.ProjectAreaMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectService;
import com.ky.ykt.utils.HttpUtils;
import com.ky.ykt.utils.NameToCode;
import com.ky.ykt.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName ProjectController
 * @Description: TODO
 * @Author czw
 * @Date 2020/2/24
 **/
@RestController
@RequestMapping("/ky-ykt/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectAreaMapper projectAreaMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryAll(params, request);
    }

    @RequestMapping(value = "queryCount", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryCount(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryCount(params);
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryPage method params are {}", params);
        RestResult restResult = projectService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    @Log(description = "资金管理发放完成操作", module = "资金管理")
    @RequestMapping(value = "/upstate", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object upstate(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The DepartmentController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                ProjectEntity projectEntity = new ProjectEntity();
                projectEntity.setId(id);
                projectEntity.setState(1);
                projectEntity.setEndTime(new Date());
                projectService.update(projectEntity);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object upload(@RequestParam MultipartFile file, HttpServletRequest request) {
        String fileName = file.getOriginalFilename();
        String uploadDirRealPath = PathUtil.getClasspath() + "projectFile";
        File filePath = new File(uploadDirRealPath);
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        String filePathName = uploadDirRealPath + "/" + fileName;
        try {
            //拿到输出流，同时重命名上传的文件

            FileOutputStream os = new FileOutputStream(filePathName);
            //拿到上传文件的输入流
            FileInputStream in = (FileInputStream) file.getInputStream();
            //以写字节的方式写文件
            int b = 0;
            while ((b = in.read()) != -1) {
                os.write(b);
            }
            os.flush();
            os.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePathName;
    }


    @Log(description = "项目管理新增，修改操作", module = "项目管理")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectEntity projectEntity, HttpServletRequest request) {
        logger.info("The ProjectController saveOrUpdate method params are {}", projectEntity);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (StringUtils.isNotEmpty(projectEntity.getId())) {
            projectEntity.setPaymentDepartment(user.getDepartmentId());
            return projectService.update(projectEntity);
        } else {
            projectEntity.setId(null);
            projectEntity.setSurplusAmount(projectEntity.getTotalAmount());
            NameToCode nameToCode = new NameToCode();
            String allFirstLetter = nameToCode.getAllFirstLetter(projectEntity.getProjectName());
            projectEntity.setProjectCode(allFirstLetter);
            projectEntity.setProjectType(allFirstLetter);
            projectEntity.setPaymentDepartment(user.getDepartmentId());
            projectEntity.setOperUser(user.getId());
            projectEntity.setOperDepartment(user.getDepartmentId());
            projectEntity.setPaymentAmount(BigDecimal.ZERO);
            return projectService.add(projectEntity);
        }
    }

    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public ProjectEntity select(String id) {
        logger.info("The ProjectController select method params are {}", id);
        RestResult restResult = projectService._get(id);
        ProjectEntity data = (ProjectEntity) restResult.getData();
        return data;
    }

    /**
     * 删除多个
     */
    @Log(description = "资金下达删除操作", module = "资金下达")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController deleteMoney method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectService.delete(id);
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

    /**
     * 删除多个
     */
    @RequestMapping(value = "selectHomeNum", method = RequestMethod.GET)
    public Object selectHomeNum(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        return projectService.selectHomeNum(request);
    }

    @RequestMapping(value = "queryAllProject", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryAllProject(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryByParams method params are {}", params);
        return projectService.queryAllProject(params, request);
    }

    @Log(description = "乡镇明细增加，修改", module = "项目管理")
    @RequestMapping(value = "/saveProjectAreas", method = RequestMethod.POST)
    public Object saveProjectAreas(@RequestBody List<ProjectAreaEntity> areaAmountList, HttpServletRequest request) {
        logger.info("The ProjectController saveProjectAreas method params are {}", areaAmountList);
        String projectId = request.getParameter("projectId");
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        for (int i = 0; i <areaAmountList.size() ; i++) {
            ProjectAreaEntity projectAreaEntity = new ProjectAreaEntity();
            Map<Object, Object> map = new HashMap<>();
            map.put("projectId",projectId);
            map.put("userId",user.getId());
            map.put("areaId",areaAmountList.get(i).getAreaId());
            //map.put("areaAmount",areaAmountList.get(i).getAreaAmount());
            ProjectAreaEntity projectAreaEntity1 = projectAreaMapper._queryProjectAreas(map);
            if(projectAreaEntity1 == null){
                projectAreaEntity.setProjectId(projectId);
                projectAreaEntity.setAreaId(areaAmountList.get(i).getAreaId());
                projectAreaEntity.setAreaAmount(areaAmountList.get(i).getAreaAmount());
                projectAreaEntity.setUserId(user.getId());
                projectAreaEntity.setOperDepartment(user.getDepartmentId());
                projectAreaMapper._addEntity(projectAreaEntity);
            }else{
                projectAreaEntity1.setAreaAmount(areaAmountList.get(i).getAreaAmount());
                projectAreaMapper._updateEntity(projectAreaEntity1);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    @Log(description = "乡镇明细查询", module = "项目管理")
    @RequestMapping(value = "/projectAreasSelect", method = RequestMethod.GET)
    public Object projectAreasSelect(String id) {
        logger.info("The ProjectController select method params are {}", id);
        List<ProjectAreaEntity> projectAreaEntities = projectAreaMapper.selectProjectId(id);
        return projectAreaEntities;
    }

    @RequestMapping(value = "/queryMetionPage", method = RequestMethod.GET)
    public Object queryMetionPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectController queryMetionPage method params are {}", params);
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        DepartmentEntity departmentEntity = departmentMapper._get(user.getDepartmentId());
        params.put("areaId", departmentEntity.getAreaId());
        params.put("proUserId", user.getId());
        RestResult restResult = projectService.queryMetionPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }
}
