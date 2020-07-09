package com.ky.ykt.interceptor;

import com.ky.ykt.license.LicenseVerify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(Interceptor.class);

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
            throws Exception {
        // TODO Auto-generated method stub
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
        if (request.getRequestURL().toString().contains("login") || request.getRequestURL().toString().contains("wechat")) {
            LicenseVerify licenseVerify = new LicenseVerify();
            return licenseVerify.verify();
        } else {
            Object object = request.getSession().getAttribute("user");
            if (object == null) {
                //未登陆，返回登陆页面
                logger.error("登陆失效");
                response.setStatus(401);
                return false;
            } else {
            /*String uri = request.getRequestURI();
            String path = request.getServletPath();
            logger.info("The uri is {} path is {} ", uri, path);
            SysUserEntity user = (SysUserEntity) object;
            List<MenuEntity> menuEntities = user.getMenuEntities();
            for (MenuEntity menuEntity :
                    menuEntities) {
                if (!uri.contains(menuEntity.getMenuUrl())) {
                    response.setStatus(401);
                    return false;
                }
                List<MenuEntity> menuChildren = menuEntity.getMenuChildren();
                for (MenuEntity menuEntity1 :
                        menuChildren) {
                    if (!uri.contains(menuEntity1.getMenuUrl())) {
                        response.setStatus(401);
                        return false;
                    }
                }
            }*/
            }
        }

        return true;
    }
}