package com.ky.ykt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.PersonUploadEntity;
import com.ky.ykt.logUtil.Log;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.AreasService;
import com.ky.ykt.service.PersonService;
import com.ky.ykt.service.PersonUploadService;
import com.ky.ykt.service.WeChatService;
import com.ky.ykt.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lenovo
 */
@RestController
@RequestMapping("/ky-ykt/wechat")
public class WeChatController {

    private static final Logger logger = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    PersonUploadService personUploadService;
    @Autowired
    WeChatService weChatService;
    @Autowired
    PersonService personService;
    @Autowired
    AreasService areasService;

    @Log(description = "微信公众号登录操作", module = "微信登录")
    @RequestMapping(value = "/weChatLogin", method = RequestMethod.POST)
    public Object weChatLogin(HttpServletRequest request, HttpSession session) {
        Map map = new HashMap();
        String name = request.getParameter("name");
        String idCardNo = request.getParameter("idCardNo");
        String bankCardNo = request.getParameter("bankCardNo");
        logger.info("The WeChatController weChatLogin method params are {},{}", name, idCardNo, bankCardNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("idCardNo", idCardNo);
        jsonObject.put("bankCardNo", bankCardNo);
        RestResult restResult = (RestResult) weChatService.wechatLogin(jsonObject);
        if (restResult.getCode() == 10000) {
            session.setAttribute("person", restResult.getData());
            session.setMaxInactiveInterval(60 * 60);
        }
        return restResult;
    }

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryPerson", method = RequestMethod.GET)
    public Object queryPerson(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params = setDepartmentIdForMap(request, params);
        logger.info("The PersonController queryByParams method params are {}", params);
        PersonUploadEntity personUploadEntity = personService.queryPerson(params);
        //PagerResult data = (PagerResult) restResult.getData();
        //return this.toJson(data);
        return JSON.toJSONString(personUploadEntity);
    }

    public static Map setDepartmentIdForMap(HttpServletRequest request, Map params) {
        PersonUploadEntity person = (PersonUploadEntity) request.getSession().getAttribute("person");
        params.put("name", person.getName());
        params.put("idCardNo", person.getIdCardNo());
        params.put("bankCardNo", person.getBankCardNo());
        return params;
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }

    @Log(description = "微信公众号登录操作", module = "微信登录")
    @RequestMapping(value = "/wechatPerson", method = RequestMethod.POST)
    public Object wechatPerson(HttpServletRequest request, HttpSession session) {
        Map map = new HashMap();
        String name = request.getParameter("name");
        String idCardNo = request.getParameter("idCardNo");
        String bankCardNo = request.getParameter("bankCardNo");
        logger.info("The WeChatController weChatLogin method params are {},{}", name, idCardNo, bankCardNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("idCardNo", idCardNo);
        jsonObject.put("bankCardNo", bankCardNo);
        RestResult restResult = (RestResult) weChatService.wechatLogin(jsonObject);
        if (restResult.getCode() == 10000) {
            session.setAttribute("person", restResult.getData());
            session.setMaxInactiveInterval(60 * 60);
        }
        return restResult;
    }

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "queryParam", method = RequestMethod.GET)
    public Object queryParam(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        params = setDepartmentIdForMap(request, params);
        logger.info("The PersonController queryByParams method params are {}", params);
        RestResult restResult = personService.queryWechatPerson(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    @RequestMapping(value = "/queryByLevel", method = RequestMethod.GET)
    public Object queryByLevel(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The wechatController queryTowns method params are {}", params);
        return areasService.queryByLevel(params);
    }

    /**
     * 新增OR更新数据
     */
    @Log(description = "人员档案新增，修改操作", module = "人员档案")
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST, produces = "application/json;UTF-8")
    public Object saveOrUpdate(@RequestBody String body, HttpServletRequest request) {
        logger.info("The PersonUploadController saveOrUpdate method params are {}", body);
        PersonUploadEntity personUploadEntity = JSONObject.parseObject(body, PersonUploadEntity.class);
        //SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        if (personUploadEntity.getId() != null && personUploadEntity.getId().length() > 0) {
            //personUploadEntity.setDepartmentId(user.getDepartmentId());
            return personUploadService.update(personUploadEntity);
        } else {
            //personUploadEntity.setDepartmentId(user.getDepartmentId());
            return personUploadService.add(personUploadEntity);
        }
    }

}
