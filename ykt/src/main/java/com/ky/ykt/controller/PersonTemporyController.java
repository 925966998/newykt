package com.ky.ykt.controller;

import com.ky.ykt.entity.AreasEntity;
import com.ky.ykt.entity.PersonTemporyEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.PersonTemporyService;
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

/**
 * @class: monitor
 * @classDesc: 功能描述（）
 * @author: yaoWieJie
 * @createTime: 2020-03-11 10:11
 * @version: v1.0
 */
@RestController
@RequestMapping("/ky-ykt/personTemporary")
public class PersonTemporyController {

    private static final Logger logger = LoggerFactory.getLogger(PersonTemporyController.class);

    @Autowired
    PersonTemporyService personTemporyService;
    
    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController queryByParams method params are {}", params);
        return personTemporyService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Object queryById(String id) {
        logger.info("The AreasController queryById method params are {}", id);
        return personTemporyService.queryById(id);


    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "临时人员新增,修改操作", module = "临时人员管理")
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(PersonTemporyEntity personTemporyEntity,HttpServletRequest request) {
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        logger.info("The AreasController saveOrUpdate method params are {}", personTemporyEntity);
        PersonTemporyEntity personTemporyEntity1 = new PersonTemporyEntity();

        personTemporyEntity1.setUserId(user.getId());
        if (StringUtils.isNotEmpty(personTemporyEntity.getId())) {
            return personTemporyService.update(personTemporyEntity);
        } else {
            personTemporyEntity.setId(null);
            return personTemporyService.add(personTemporyEntity);
        }
    }

    /**
     * 逻辑删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "临时人员辑删除操作", module = "临时人员管理")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public Object delete(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController delete method params is {}", params);
        return personTemporyService.delete(params.get("id").toString());
    }

    /**
     * 物理删除
     */
    @Log(description = "临时人员物理删除操作", module = "临时人员")
    @RequestMapping(value = "/deleteForce", method = RequestMethod.GET)
    public Object deleteForce(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController deleteForce method params is {}", params);
        String id = params.get("id").toString();
        if (id.contains(",")) {
            String[] split = id.split(",");
            for (int i = 0; i < split.length; i++) {
                personTemporyService._deleteForce(split[i]);
            }
        } else {
            personTemporyService._deleteForce(params.get("id").toString());
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
        logger.info("The AreasController queryPage method params are {}", params);
        return personTemporyService.queryPage(params);
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @Log(description = "临时人员管理删除操作", module = "临时人员管理")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController delete method params is {}", params);
        return personTemporyService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @Log(description = "临时人员管理删除操作", module = "临时人员管理")
    @RequestMapping(value = "deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The AreasController deleteForce method params is {}", params);
        if (params.get("ids") != null) {
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id : ids
            ) {
                personTemporyService.delete(id);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }
}