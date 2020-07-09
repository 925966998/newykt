package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.ProjectSourceEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.ProjectSourceService;
import com.ky.ykt.utils.HttpUtils;
import com.ky.ykt.utils.PathUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @ClassName ProjectSourceController
 * @Description: 资金来源
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/projectSource")
public class ProjectSourceController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectSourceController.class);

    @Autowired
    ProjectSourceService projectSourceService;

    /**
     * 查询全部数据不分页
     */
    @RequestMapping(value = "queryByParams", method = RequestMethod.GET, produces = "application/json;UTF-8")
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectSourceController queryByParams method params are {}", params);
        return projectSourceService.queryAll(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "资金来源新增，修改操作", module = "资金来源")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(ProjectSourceEntity projectSourceEntity) {
        logger.info("The ProjectSourceController saveOrUpdate method params are {}", projectSourceEntity);
//        ProjectSourceEntity ProjectSourceEntity = JSONObject.parseObject(body, ProjectSourceEntity.class);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (StringUtils.isNotEmpty(projectSourceEntity.getId())) {
            projectSourceEntity.setDepartment(user.getDepartmentId());
            return projectSourceService.update(projectSourceEntity);
        } else {
            projectSourceEntity.setId(null);
            projectSourceEntity.setDepartment(user.getDepartmentId());
            projectSourceEntity.setOperDepartment(user.getDepartmentId());
            return projectSourceService.add(projectSourceEntity);
        }
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectSourceController queryPage method params are {}", params);
        RestResult restResult = projectSourceService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }


    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/select", method = RequestMethod.GET)
    public Object select(String id) {
        logger.info("The ProjectSourceController queryPage method params are {}", id);
        RestResult restResult = projectSourceService._get(id);
        ProjectSourceEntity data = (ProjectSourceEntity) restResult.getData();
        return data;
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "资金来源删除操作", module = "资金来源")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectSourceController delete method params is {}", params);
        return projectSourceService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "资金来源删除操作", module = "资金来源")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The ProjectSourceController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                projectSourceService.delete(id);
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
}
