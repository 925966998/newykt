package com.ky.ykt.service;

import com.alibaba.fastjson.JSONObject;
import com.ky.ykt.comparator.MenuComparator;
import com.ky.ykt.entity.MenuEntity;
import com.ky.ykt.entity.SysUserEntity;
import com.ky.ykt.mapper.MenuMapper;
import com.ky.ykt.mapper.RoleMenuMapper;
import com.ky.ykt.mapper.SysUserMapper;
import com.ky.ykt.mybatis.RestResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LoginService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;

    public Object login(JSONObject parseObject) {
        if (parseObject.containsKey("userName") && parseObject.containsKey("password")) {
            SysUserEntity sysUserEntity = sysUserMapper.queryByUserName(parseObject.getString("userName"));
            if (sysUserEntity != null) {
                if (sysUserEntity.getStatus() > 0) {
                    return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "该用户被停用");
                }
                if (sysUserEntity.getPassword().equals(parseObject.getString("password"))) {
                    MenuComparator menuComparator = new MenuComparator();
                    List<String> strings = roleMenuMapper.queryMenuIdByRoleId(sysUserEntity.getRoleId());
                    List<MenuEntity> menuEntities = new ArrayList<>();
                    for (String menuId : strings) {
                        MenuEntity menuEntity = menuMapper._get(menuId);
                        if (menuEntity!=null&&StringUtils.isEmpty(menuEntity.getParentId())) {
                            List<MenuEntity> children = new ArrayList();
                            List<MenuEntity> menuEntities1 = menuMapper.queryByPid(menuEntity.getId());
                            for (MenuEntity menuEntity1 : menuEntities1) {
                                if (strings.contains(menuEntity1.getId()))
                                    children.add(menuEntity1);
                            }
                            Collections.sort(children, menuComparator);
                            menuEntity.setMenuChildren(children);
                            menuEntities.add(menuEntity);
                        }
                    }
                    //调用排序方法
                    Collections.sort(menuEntities, menuComparator);
                    sysUserEntity.setMenuEntities(menuEntities);
                    return new RestResult(RestResult.SUCCESS_CODE, RestResult.SUCCESS_MSG, sysUserEntity);
                }
            } else {
                return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "用户名密码错误");
            }

        } else {
            return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "用户名密码错误");
        }
        return new RestResult(RestResult.ERROR_CODE, RestResult.ERROR_MSG, "用户名密码错误");
    }
}
