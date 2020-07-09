package com.ky.ykt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import com.ky.ykt.service.UserLogService;
import com.ky.ykt.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName UserLogController
 * @Description: 用户操作日志管理
 * @Author czw
 * @Date 2020/2/18
 **/
@RestController
@RequestMapping("/ky-ykt/userLog")
public class UserLogController {
    private static final Logger logger = LoggerFactory.getLogger(UserLogController.class);

    @Autowired
    UserLogService userLogService;

    /**
     * 根据条件查询数据（不分页）
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryByParams", method = RequestMethod.GET)
    public Object queryByParams(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The UserLogController queryByParams method params are {}", params);
        return userLogService.queryAll(params);
    }

    /**
     * 根据Id查询数据
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/queryById", method = RequestMethod.GET)
    public Object queryById(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The UserLogController queryByParams method params are {}", params);
        return userLogService.get(params);
    }

    /**
     * 删除
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/deleteOne", method = RequestMethod.GET)
    public Object deleteOne(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The UserLogController delete method params is {}", params);
        return userLogService.delete(params.get("id").toString());
    }

    /**
     * 删除多个
     */
    @RequestMapping(value = "/deleteMoney", method = RequestMethod.GET)
    public Object deleteMoney(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The UserLogController deleteForce method params is {}", params);
        if(params.get("ids")!=null){
            String idsStr = params.get("ids").toString();
            String[] ids = idsStr.split(",");
            for (String id:ids
                 ) {
                userLogService.delete(id);
            }
        }
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG);
    }

    /**
     * 根据条件分页查询
     */
    @RequestMapping(value = "/queryPage", method = RequestMethod.GET)
    public Object queryPage(HttpServletRequest request) {
        Map params = HttpUtils.getParams(request);
        logger.info("The UserLogController queryPage method params are {}", params);
        RestResult restResult = userLogService.queryPage(params);
        PagerResult data = (PagerResult) restResult.getData();
        return this.toJson(data);
    }

    public JSONObject toJson(PagerResult data) {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("total", data.getTotalItemsCount());
        jsonObj.put("rows", data.getItems());
        return jsonObj;
    }
}
