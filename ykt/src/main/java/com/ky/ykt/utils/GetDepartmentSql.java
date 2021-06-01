package com.ky.ykt.utils;

import com.ky.ykt.entity.SysUserEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName GetDepartmentSql
 * @Description: TODO
 * @Author czw
 * @Date 2020/3/7
 **/
public class GetDepartmentSql {
    public static StringBuilder getUserBuilder(String paramString) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        SysUserEntity user = (SysUserEntity) request.getSession().getAttribute("user");
        StringBuilder builder = new StringBuilder("");
        //if (user.getUserName().equals("admin") || user.getUserName().equals("syxczj")) {
        if (user.getUserName().equals("admin") || user.getRoleId().equals("07e74af9-a3dd-4093-b84f-9b3a7d249a19")) {
            return builder;
        } else {
            // and  p.operDepartment = '" + user.getDepartmentId()+"'
            builder.append(" and " + paramString + "  = '" + user.getDepartmentId() + "'");
            return builder;
        }
    }
}
