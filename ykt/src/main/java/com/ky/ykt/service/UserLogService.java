package com.ky.ykt.service;


import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.entity.UserLogEntity;
import com.ky.ykt.mapper.UserLogMapper;
import com.ky.ykt.mybatis.PagerResult;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.collections.MapUtils;
import org.apache.ibatis.javassist.*;
import org.apache.ibatis.javassist.bytecode.CodeAttribute;
import org.apache.ibatis.javassist.bytecode.LocalVariableAttribute;
import org.apache.ibatis.javassist.bytecode.MethodInfo;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserLogService
 * @Description: 用户操作记录
 * @Author czw
 * @Date 2020/2/18
 **/
@Service
public class UserLogService {

    private static final Logger logger = LoggerFactory.getLogger(UserLogService.class);
    //    private static final String LOG_CONTENT = "操作日志内容为";
//    private static final String LOG_CONTENT = "操作日志内容为\"[模块]:%s,[操作]:%s,[参数]:%s,[IP]:%s\"";
    private static final String LOG_CONTENT = "[模块]:%s,[操作]:%s,[参数]:%s,[IP]:%s";

    @Autowired
    UserLogMapper userLogMapper;

    public void put(JoinPoint joinPoint, String methodName, String module, String description) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            UserLogEntity log = new UserLogEntity();
            // 获取当前登录用户
            SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");

            String ip = this.getIpAddr(request);
            log.setUserName(user.getUserName());
            log.setModule(module);
            log.setDescription(description);
            log.setIp(ip);
            logger.info("日志注入");
            log.setContent(operateContent(joinPoint, methodName, ip, request, module, description));
            //保存日志
            userLogMapper._addEntity(log);
            logger.info("日志注入完成");
            //保存完成
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String operateContent(JoinPoint joinPoint, String methodName, String ip, HttpServletRequest request, String module, String description) throws ClassNotFoundException, NotFoundException {
        String className = joinPoint.getTarget().getClass().getName();
        Object[] params = joinPoint.getArgs();
        String classType = joinPoint.getTarget().getClass().getName();
        Class<?> clazz = Class.forName(classType);
        String clazzName = clazz.getName();
        Map<String, Object> nameAndArgs = getFieldsName(this.getClass(), clazzName, methodName, params);
        StringBuffer bf = new StringBuffer();
        if (!CollectionUtils.isEmpty(nameAndArgs)) {
            Iterator it = nameAndArgs.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                if (!StringUtils.isEmpty(key) && key.equals("request")) {
                    Map<String, String[]> parameterMap = request.getParameterMap();
                    for (Iterator iter = parameterMap.entrySet().iterator(); iter.hasNext(); ) {
                        Map.Entry element = (Map.Entry) iter.next();
                        Object strKey = element.getKey();
                        String[] strArr = (String[]) element.getValue();
                        String strValue = "";
                        if (strArr != null && strArr.length > 0) {
                            strValue = strArr[0];
                        }
                        bf.append(strKey).append("=");
                        bf.append(strValue).append("&");
                    }
                } else {
                    String value = JSONObject.toJSONString(entry.getValue());
                    bf.append(key).append("=");
                    bf.append(value).append("&");
                }
            }
        }
        if (StringUtils.isEmpty(bf.toString())) {
            bf.append(request.getQueryString());
        }
        return String.format(LOG_CONTENT, module, description, bf.toString(), ip);
    }

    private Map<String, Object> getFieldsName(Class cls, String clazzName, String methodName, Object[] args) throws NotFoundException {
        Map<String, Object> map = new HashMap<String, Object>();

        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);
        pool.insertClassPath(classPath);

        CtClass cc = pool.get(clazzName);
        CtMethod cm = cc.getDeclaredMethod(methodName);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            // exception
            return map;
        }
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < cm.getParameterTypes().length; i++) {
            map.put(attr.variableName(i + pos), args[i]);//paramNames即参数名
        }
        return map;
    }

    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { //"***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }


    /**
     * 查询全部
     *
     * @param params
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Object queryAll(Map params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._queryAll(params));
    }

    /**
     * currentPage : 当前第几页，默认1 pageSize : 每页多少条，默认10
     *
     * @param params
     * @return
     */
    public RestResult queryPage(Map params) {
        List<UserLogEntity> list = userLogMapper._queryPage(params);
        long count = userLogMapper._queryCount(params);
        PagerResult pagerResult = new PagerResult(list, count, MapUtils.getLongValue(params, "page"),
                MapUtils.getLongValue(params, "rows"));
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, pagerResult);
    }

    /**
     * 按id查询 参数 要查询的记录的id
     */
    public Object get(Map<String, String> params) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._get(params.get("id")));
    }


    /**
     * 新增 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object add(UserLogEntity entity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._addEntity(entity));
    }

    /**
     * 更新 参数 map里的key为属性名（字段首字母小写） value为要插入的key的value
     */
    public Object update(UserLogEntity userLogEntity) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._updateEntity(userLogEntity));
    }

    /**
     * 逻辑删除
     */
    public Object delete(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._delete(id));
    }

    /**
     * 物理删除
     */
    public Object _deleteForce(String id) {
        return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, userLogMapper._deleteForce(id));
    }

}
